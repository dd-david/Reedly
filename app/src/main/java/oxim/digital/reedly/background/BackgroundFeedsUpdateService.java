package oxim.digital.reedly.background;

import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

import javax.inject.Inject;

import oxim.digital.reedly.dagger.application.ReedlyApplication;
import oxim.digital.reedly.device.notification.Notifications;
import oxim.digital.reedly.domain.interactor.article.GetUnreadArticlesCountUseCase;
import oxim.digital.reedly.domain.interactor.feed.GetUserFeedsUseCase;
import oxim.digital.reedly.domain.interactor.feed.update.UpdateFeedUseCase;
import rx.Observable;
import rx.schedulers.Schedulers;

public final class BackgroundFeedsUpdateService extends JobService {

    private static final String TAG = "app-BgFeedUpdateService";
    private static final int NEW_ARTICLES_NOTIFICATION_ID = 1832;

    @Inject
    GetUserFeedsUseCase getUserFeedsUseCase;

    @Inject
    UpdateFeedUseCase updateFeedUseCase;

    @Inject
    GetUnreadArticlesCountUseCase getUnreadArticlesCountUseCase;

    @Inject
    Notifications notifications;

    @Inject
    NotificationFactory notificationFactory;

    @Inject
    PendingIntent notificationPendingIntent;

    @Override
    public void onCreate() {
        super.onCreate();

        // 이게 의존성을 주입하는 코드가 되는거 같음
        // ReedlyApplication 이 의존성을 주입하는 main() 역할을 함
        ReedlyApplication.from(getApplication())    // Context(of Application) -> ReedlyApplication
                .getApplicationComponent()          // ReedlyApplication extends Application #getApplicationComponent -> ApplicationComponent
                .inject(this);                      // ApplicationComponent extends ApplicationComponentInjects #inject -> void
    }

    @Override
    public boolean onStartJob(final JobParameters jobParameters) {
        Log.d(TAG, "onStartJob");
        getUnreadArticlesCountUseCase.execute()         // Single<Long>
                .subscribeOn(Schedulers.io())           // 위 Observable Stream 부터 별도의 thread 로 동작하도록 세팅
                .subscribe(
                        unreadCount -> onUnreadItemsCount(unreadCount, jobParameters),
                        throwable -> handleError(throwable, jobParameters));

        // 이 JobService 가 별도의 thread 로 빠져서 동작함을 의미함.
        // 그렇다면 JobFinished() 호출은 언제?
        return true;
    }

    /* NORMAL CASE */

    private void onUnreadItemsCount(final long unreadItemsCount, final JobParameters jobParameters) {
        Log.d(TAG, "onUnreadItemsCount [unread item count=" + unreadItemsCount + "]");
        /*
        * 1. 사용자의 Feeds 를 읽어와서 : List<Feed>
        * 2. Feed 를 업데이트 합니다 : while(feed.hasNext())
        * (1)------L-->|
        *          |                   (Observable.from)
        * (2)------O-----O-----O---->|
        *                            | (Observable.toCompletable)
        * (3)----------------------->|
        *
        * 3. 2번을 다 수행하면, 이 클래스 내 onUpdateFeeds 를 실행합니다 (Feed Update 가 다 됐음!)
        *
        * */
        getUserFeedsUseCase.execute()                                                           // Single<List<Feed>>
                           .flatMapObservable(Observable::from)                                 // Observable<Feed>
                           .flatMap(feed -> updateFeedUseCase.execute(feed).toObservable())     // Completable -> Observable<T>
                           .toCompletable()                                                     // (*) 1.x : up-stream 의 모든 항목이 끝나면, completable (홀딩 해주는 역할을 하네)
                           .subscribeOn(Schedulers.io())                                        // 이걸 또 다른 thread 에서 동작시킬 필요가 있나?
                           .subscribe( /* 성공한 경우 */ () -> onUpdatedFeeds(unreadItemsCount, jobParameters),
                                       /* 실패한 경우 */ throwable -> handleError(throwable, jobParameters));
    }

    private void onUpdatedFeeds(final long unreadItemsCount, final JobParameters jobParameters) {
        Log.d(TAG, "onUpdatedFeeds [unread item count=" + unreadItemsCount + "]");
        /*
        * 위에서 onUnreadItemsCount 가 호출해줌
        * 1. unread article 갯수를 읽어옴 : Single<Long>
        * 2. 읽어온 개수를 -> onNewUnreadCount() 에 넘겨줍니다
        *
        * +3. doOnSuccess()를 통해서, 후속처리 jobFinished 를 처리합니다.
        * TODO: 궁금한건 위치와 동작시점! onSuccess(int)->onNewUnreadCount() 실행시키는데, 이게 끝나고 jobFinished 가 되어야함
        *
        * */
        getUnreadArticlesCountUseCase.execute()                                                         // Single<Long>
                                     .subscribeOn(Schedulers.io())                                      // 매번 별개의 Schedulers.io()에서 동작시킬 필요 없어보이는데
                                     .doOnSuccess(c -> jobFinished(jobParameters, false))               // 성공케이스의 jobFinished 는 다음과 같이 불린다
                                     .subscribe(newUnreadCount -> onNewUnreadCount(unreadItemsCount, newUnreadCount),
                                                throwable -> handleError(throwable, jobParameters));
    }

    private void onNewUnreadCount(final long oldCount, final long newCount) {
        Log.d(TAG, "onNewUnreadCount [old count=" + oldCount + ", new count=" + newCount + "]");
        /*
        * 새로 갱신한 정보가 (안읽은 기사) 더 많으면 -> 사용자 노티
        * */
        if (newCount > oldCount) {
            showNewArticlesNotification();
        }
    }

    private void showNewArticlesNotification() {
        Log.d(TAG, "showNewArticlesNotification");
        notifications.showNotification(NEW_ARTICLES_NOTIFICATION_ID,
                notificationFactory.createNewArticlesNotification(notificationPendingIntent));
    }

    /* ERROR CASE */
    // 에러 케이스의 경우! stacktrace 를 출력하고 jobFinished
    private void handleError(final Throwable throwable, final JobParameters jobParameters) {
        throwable.printStackTrace();
        jobFinished(jobParameters, false);
    }

    @Override
    public boolean onStopJob(final JobParameters jobParameters) {
        return false;
    }
}

package oxim.digital.reedly.dagger.application;

import android.app.Application;
import android.content.Context;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;

import com.facebook.stetho.Stetho;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

import javax.inject.Inject;

import oxim.digital.reedly.BuildConfig;
import oxim.digital.reedly.dagger.ComponentFactory;
import oxim.digital.reedly.domain.interactor.feed.update.EnableBackgroundFeedUpdatesUseCase;
import oxim.digital.reedly.domain.interactor.feed.update.ShouldUpdateFeedsInBackgroundUseCase;
import rx.Completable;

/**

 Copyright 2017 Mihael Franceković

 Whole application code licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.

 */
public final class ReedlyApplication extends Application {

    private static final String TAG = "app-ReedlyApplication";

    // domain 모듈에 있는 UseCase 또한 Inject 를 활용해야 하나?
    // 동일 depth(layer) 에 있는 녀석들끼리 서로 알아야 할때, 의존성 주입을 하는것 뿐 아니라
    // 내부 계층에 있는 UseCase 를 의존성 주입 하는 이유는?
    @Inject
    ShouldUpdateFeedsInBackgroundUseCase shouldUpdateFeedsInBackgroundUseCase;

    @Inject
    EnableBackgroundFeedUpdatesUseCase enableBackgroundFeedUpdatesUseCase;

    private ApplicationComponent applicationComponent;

    public static ReedlyApplication from(final Context context) {
        return (ReedlyApplication) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "OnCreate");

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        // Dagger2 관련
        applicationComponent = ComponentFactory.createApplicationComponent(this);
        applicationComponent.inject(this);

        // DBFlow 관련
        FlowManager.init(new FlowConfig.Builder(this).build());
        checkForBackgroundUpdate();

        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this);
        }
    }

    private void checkForBackgroundUpdate() {
        Log.d(TAG, "checkForBackgroundUpdate");
        shouldUpdateFeedsInBackgroundUseCase.execute()          // Single<Boolean>
                .flatMap(shouldUpdate -> (                      // Completable
                          (shouldUpdate) ?
                              enableBackgroundFeedUpdatesUseCase.execute() :
                              Completable.complete()
                         )
                         .toSingleDefault(true))                // Single<Boolean>
                         .subscribe();                          // Single.subscribe(void) : Single 일 구독하지만 후속처리는 하지 않음

        // 이 동작은 shoudUpdateFeedsInBackgroundUseCase 에서 true/false 를 받은 후
        // enableBackgroundFeedUpdatesUseCase 에서 true 일 경우만, 함수를 수행하는 역할을 합니다.
        // TODO: 그럼 더 단순하게 코드를 구현하는건 문제인가?
        // (예) shouldUpdateFeedsInBackgroundUseCase.execute()
        //              .subscribe(shouldUpdate -> {
        //                  if (shouldUpdate) enableBackgroundFeedUpdatesUseCase.execute();
        //              });
        // true 인 경우에만 동작을 하고, 실패한 경우 아무 동작을 하지 않고, 그 여부를 알 필요가 없는데 길게 구현 할 필요가 있나?
    }

    public ApplicationComponent getApplicationComponent() {
        Log.d(TAG, "getApplicationComponent");
        return applicationComponent;
    }
}

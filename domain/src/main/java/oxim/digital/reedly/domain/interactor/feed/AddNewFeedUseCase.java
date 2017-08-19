package oxim.digital.reedly.domain.interactor.feed;

import oxim.digital.reedly.domain.interactor.type.CompletableUseCaseWithParameter;
import oxim.digital.reedly.domain.repository.FeedRepository;
import rx.Completable;

// UseCase 자체는 concrete class 로 구현됩니다.
public final class AddNewFeedUseCase implements CompletableUseCaseWithParameter<String> {

    private final FeedRepository feedRepository;

    // 이렇게 FeedRepository 는 어디서 할당해주나?
    // 지금보면 dependency injection 으로 할당해주는거 같은데?
    // UseCase, Repository는 서로가 같은 계층에 있고 다른 모듈이기 때문에
    // 의존성 주입으로 서로를 알게 하는건가? 확인이 필요함
    public AddNewFeedUseCase(final FeedRepository feedRepository) {
        this.feedRepository = feedRepository;
    }

    @Override
    public Completable execute(final String feedUrl) {
        return feedRepository.createNewFeed(feedUrl);
    }
}

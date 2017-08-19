package oxim.digital.reedly.domain.interactor.type;

import rx.Completable;

// 일반 UseCaseWithParameter 의 Completable 버전
public interface CompletableUseCaseWithParameter<P> {

    Completable execute(P parameter);
}

package oxim.digital.reedly.domain.interactor.type;

import rx.Single;

// 일반 UseCaseWithParameter 의 Single 모드
public interface SingleUseCaseWithParameter<P, R> {

    Single<R> execute(P parameter);
}

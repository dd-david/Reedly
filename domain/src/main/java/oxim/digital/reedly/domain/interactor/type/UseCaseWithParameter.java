package oxim.digital.reedly.domain.interactor.type;

import rx.Observable;

// 일반적인 UseCase 를 정의한 interface, with params
public interface UseCaseWithParameter<P, R> {

    Observable<R> execute(P parameter);
}

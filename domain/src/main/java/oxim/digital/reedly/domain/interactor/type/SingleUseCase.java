package oxim.digital.reedly.domain.interactor.type;

import rx.Single;

// 일반 UseCase 의 Single 모드
public interface SingleUseCase<T> {

    Single<T> execute();
}

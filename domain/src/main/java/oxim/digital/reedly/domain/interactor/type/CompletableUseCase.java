package oxim.digital.reedly.domain.interactor.type;

import rx.Completable;

// 일반 UseCase 의 Completable 버전
// CompletableObserver -> onComplete(), onError()
// 뭔가 동작에 대해서 void 리턴은 두지 않는건가?
// Completable 로 들을 내용들은 어떤게 있을까?
public interface CompletableUseCase {

    Completable execute();
}

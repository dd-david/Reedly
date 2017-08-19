package oxim.digital.reedly.domain.interactor.type;

import rx.Observable;


// 일반적인 UseCase 를 정의한 interface, without params
public interface UseCase<T> {

    // input port : execute 를 호출하는거고
    // output port : 결과로 리턴되는 Observable<T>를 구독하는 것
    Observable<T> execute();
}

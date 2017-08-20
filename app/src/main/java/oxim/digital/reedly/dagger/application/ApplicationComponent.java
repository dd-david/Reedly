package oxim.digital.reedly.dagger.application;

import javax.inject.Singleton;

import dagger.Component;
import oxim.digital.reedly.dagger.application.module.ApplicationModule;
import oxim.digital.reedly.dagger.application.module.DataModule;
import oxim.digital.reedly.dagger.application.module.MappersModule;
import oxim.digital.reedly.dagger.application.module.ServiceModule;
import oxim.digital.reedly.dagger.application.module.ThreadingModule;
import oxim.digital.reedly.dagger.application.module.UseCaseModule;
import oxim.digital.reedly.dagger.application.module.UtilsModule;

@Singleton
@Component(
        modules = {
                ApplicationModule.class,
                ThreadingModule.class,
                UtilsModule.class,
                UseCaseModule.class,
                DataModule.class,
                MappersModule.class,
                ServiceModule.class
        }
)

// 두 인터페이스를 상속받고 있는데, 역할의 목적은 무엇인가?
public interface ApplicationComponent extends ApplicationComponentInjects, ApplicationComponentExposes {

    // interface 내부에 final class (concrete class)가 존재해도 상관없나?
    final class Initializer {

        static public ApplicationComponent init(final ReedlyApplication reedlyApplication) {
            /*
            * 1. @Component ApplicationComponent --(Dagger2)--> DaggerApplicationComponent (자동생성)
            * 2. DaggerApplicationComponent.builder() : DaggerApplicationComponent 객체의 생성
            * 3. 의존성을 주입합니다
            *     .applicationModule(new ApplicationModule())
            *     .threadingModule(new ThreadingModule())
            *     ....
            *     모듈이 함수명이며, 파라미터로 들어가는 객체도 new 모듈() 형태이다
            * 4. build()를 통해 의존성 주입의 끝!
            * */
            return DaggerApplicationComponent.builder()
                                             .applicationModule(new ApplicationModule(reedlyApplication))
                                             .threadingModule(new ThreadingModule())
                                             .utilsModule(new UtilsModule())
                                             .useCaseModule(new UseCaseModule())
                                             .dataModule(new DataModule())
                                             .mappersModule(new MappersModule())
                                             .serviceModule(new ServiceModule())
                                             .build();
        }

        private Initializer() {

        }
    }
}

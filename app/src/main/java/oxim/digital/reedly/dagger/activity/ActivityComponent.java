package oxim.digital.reedly.dagger.activity;

import dagger.Component;
import oxim.digital.reedly.dagger.activity.module.ActivityModule;
import oxim.digital.reedly.dagger.application.ApplicationComponent;

/*
* @Component
* interface MyComponent { ... }
* -> DaggerMyComponent 구현부를 자동으로 생성합니다.
*
* */

@ActivityScope
@Component(
        dependencies = ApplicationComponent.class,
        modules = {
                ActivityModule.class
        }
)
public interface ActivityComponent extends ActivityComponentInjects, ActivityComponentExposes {

    final class Initializer {

        static public ActivityComponent init(final DaggerActivity daggerActivity, final ApplicationComponent applicationComponent) {
            return DaggerActivityComponent.builder()
                                          .applicationComponent(applicationComponent)
                                          .activityModule(new ActivityModule(daggerActivity))
                                          .build();
        }

        private Initializer() {
        }
    }
}

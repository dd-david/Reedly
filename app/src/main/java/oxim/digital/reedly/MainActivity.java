package oxim.digital.reedly;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import javax.inject.Inject;

import oxim.digital.reedly.base.BaseActivity;
import oxim.digital.reedly.base.ScopedPresenter;
import oxim.digital.reedly.dagger.activity.ActivityComponent;
import oxim.digital.reedly.ui.MainPresenter;
import oxim.digital.reedly.ui.feed.subscription.UserSubscriptionsFragment;
import oxim.digital.reedly.util.ActivityUtils;

public final class MainActivity extends BaseActivity {

    /*
    * Dagger2
    * dependency injector for Android and Java
    *
    * Github : https://github.com/google/dagger
    * Documentation : https://google.github.io/dagger/
    * Javadoc : https://google.github.io/dagger/api/2.0/
    *
    * @Inject
    * Constructor(Required param){ ... }
    * 생성자에 @Inject 를 사용하면, 객체가 생성될때 Dagger2 가 알아서 Required param 을 가져와서 만듭니다
    *
    * @Inject Required filed;
    * 1. 생성자에 @Inject 가 없는경우 :
    *     Dagger 가 해당 filed 가 사용될때, 의존성을 주입시킵니다.
    *     그러나 새로운 객체를 만드는 것은 아닙니다.
    *
    * 2. 생성자(no param) 을 만들고 앞에 @Inject를 붙이면
    *     객체도 함께 만듭니다.
    *
    * method 도 @Inject 를 지원하지만. 생성자/필드가 더 선호됩니다.
    *
    * 클래스내에 @Inject 란 annotation 이 없으면, 당연히 Dagger 에 의해서 만들지 않습니다.
    *
    *
    *
    *
    * 그럼 주입을 시켜주는건 누가 해주나?
    * @Provider annotation 이 결국 Dependency Injection 을 만족시킵니다.
    *
    * // Heater 가 필요할때는 아래의 함수가 언제든 실행됩니다.
    * @Provider
    * static Heater provideHeater() {
    *   return new ElectricHeater();
    * }
    * // Pump 가 필요할때는 아래의 함수가 언제든 실행됩니다.
    * @Provider
    * static Pump providePump(Thermosiphon pump) {
    *   return pump;
    * }
    *
    * 그리고 @Provider 가 포함되어있는 함수는 꼭! -> @Module annotation 을 클래스에 붙여야 합니다.
    * */



    @Inject
    FragmentManager fragmentManager;

    @Inject
    ActivityUtils activityUtils;

    @Inject
    MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            activityUtils.addFragmentWithTagToActivity(fragmentManager, UserSubscriptionsFragment.newInstance(), R.id.activity_container, UserSubscriptionsFragment.TAG);
        }
    }

    @Override
    protected ScopedPresenter getPresenter() {
        return presenter;
    }

    @Override
    protected void inject(final ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }
}

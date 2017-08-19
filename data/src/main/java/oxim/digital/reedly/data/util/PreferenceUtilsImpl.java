package oxim.digital.reedly.data.util;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceUtilsImpl implements PreferenceUtils {

    private static final String USER_PREFERENCES = "user_preferences";

    private static final String KEY_SHOULD_UPDATE_FEEDS_IN_BACKGROUND = "key_should_update_feed_in_background";

    private final SharedPreferences preferences;

    // util 클래스들이 singleton 이 아니라, 이렇게 오픈되는게 더 나은건가?
    // 아래 두 함수는 file io 이니 background 에서 돌려야 하는데, 그 처리는 어디서?
    public PreferenceUtilsImpl(final Context context) {
        this.preferences = context.getSharedPreferences(USER_PREFERENCES, Context.MODE_PRIVATE);
    }

    // SP 에서 읽어오기
    @Override
    public boolean shouldUpdateFeedsInBackground() {
        return preferences.getBoolean(KEY_SHOULD_UPDATE_FEEDS_IN_BACKGROUND, true);
    }

    // SP 에 저장하기
    @Override
    public void setShouldUpdateFeedsInBackground(final boolean shouldUpdate) {
        preferences.edit()
                   .putBoolean(KEY_SHOULD_UPDATE_FEEDS_IN_BACKGROUND, shouldUpdate)
                   .apply();
    }
}

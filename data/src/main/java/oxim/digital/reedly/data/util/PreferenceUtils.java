package oxim.digital.reedly.data.util;

// DB,API 에 해당하는 (outer layer) 함수들의 인테페이스
// 누구에게 공유되기 위해서 interface 로 지정한건가?
public interface PreferenceUtils {

    boolean shouldUpdateFeedsInBackground();

    void setShouldUpdateFeedsInBackground(boolean shouldUpdate);
}

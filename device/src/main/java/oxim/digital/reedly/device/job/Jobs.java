package oxim.digital.reedly.device.job;

import android.app.job.JobInfo;

// 앞선 설명에서 Device module 의 interface 도 --> domain module 에 넣으라고 했는데
// Job이나 Notifiacton 같은 interface 는 이렇게 내부에 두나?
// 그럼 어떻게, 누가 활용하나? app module 에서 활용하는 것도 dependency injection 을 활용?
public interface Jobs {

    int schedule(JobInfo jobInfo);

    void cancel(int jobId);
}

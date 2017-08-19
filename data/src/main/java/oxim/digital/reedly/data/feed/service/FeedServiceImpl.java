package oxim.digital.reedly.data.feed.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import oxim.digital.reedly.data.feed.service.model.ApiFeed;
import oxim.digital.reedly.data.feed.service.parser.FeedParser;
import oxim.digital.reedly.data.util.CurrentTimeProvider;
import rx.Single;

public final class FeedServiceImpl implements FeedService {

    private final FeedParser feedParser;

    public FeedServiceImpl(final FeedParser feedParser) {
        this.feedParser = feedParser;
    }

    @Override
    public Single<ApiFeed> fetchFeed(final String feedUrl) {
        try {
            final InputStream inputStream = new URL(feedUrl).openConnection().getInputStream();
            return feedParser.parseFeed(inputStream, feedUrl)
                             .doOnSuccess(feed -> closeStream(inputStream))
                             .doOnError(throwable -> closeStream(inputStream));
            // 아주 좋은 부분입니다!!!
            // 내가 stream 을 리턴해주면서, 내 후속처리를 해야하는 상황이 있지
            // 예를들어 Service 를 처리하다가, 끝나면 stopSelf() 를 부르고 싶은 상황
            // 그럴때 .doOnSuccess(close) .doOnError(close) 해주면 좋은거 같음
        } catch (IOException e) {
            e.printStackTrace();
            return Single.error(e);
        }
    }

    private void closeStream(final InputStream inputStream) {
        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

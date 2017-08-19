package oxim.digital.reedly.data.feed.db.model;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.annotation.Unique;
import com.raizlabs.android.dbflow.structure.BaseModel;

import oxim.digital.reedly.data.feed.db.definition.FeedDatabase;

/*
*
* DBFlow : ORM(Object-relational mapper) Android Db
* https://github.com/Raizlabs/DBFlow
*
* Guideline : https://agrosner.gitbooks.io/dbflow/content/
*
* Table 을 생성하는 방법 또한 심플합니다.
*
* @Table(database = FeedDatabase.class)     // DatabaseDefinition 이 설정된 클래스를 기입하고
* public final class FeedModel {
*     @Column
*     @PrimaryKey(autoincrement = true)
*     int id;
*
*     @Column
*     String title;
*
* 일반적으로 조작하는 방법은 다음과 같습니다
*   FreeModel fm = new FreeModel(1, "title");
*   ModelAdapter<FreeModel> adapter = FlowManager.getModelAdapter(FreeModel.class);
*   adapter.insert(fm);
*
*   fm.title = "title-changed";
*   adapter.update(fm);
*   adapter.delete(fm);
*
* 흥미로운건 extends BaseModel 혹은 implements Model 을 하면 더 심플해짐
*   FreeModel fm = new FreeModel(1, "title");
*   fm.insert();
*
* 안의 데이터를 읽어오는 방법은 다음과 같습니다
* List<User> users = SQLite.select().from(User.class).where(User_Table.age.greaterThan(18)).queryList();
*
* SQLite.select().from(User.class).where(User_Table.age.greaterThan(18)).async()
*     .queryListCallback((QueryTransaction transaction, @NonNull CursorResult<User> result) -> {
*              // called when query returns on UI thread
*              try {
*                List<User> users = result.toList();
*                // do something with users
*              } finally {
*                result.close();
*              }
*            })
*     .error((Transaction transaction, Throwable error) -> {
*              // handle any errors
*         })
*     .execute();
* */


@Table(database = FeedDatabase.class)
public final class FeedModel extends BaseModel {

    @Column
    @PrimaryKey(autoincrement = true)
    int id;

    @Column
    String title;

    @Column
    String imageUrl;

    @Column
    String pageLink;

    @Column
    String description;

    @Column
    @Unique(onUniqueConflict = ConflictAction.FAIL)
    String url;

    public FeedModel() { }

    public FeedModel(final String title, final String imageUrl, final String pageLink, final String description, final String url) {
        this(0, title, imageUrl, pageLink, description, url);
    }

    public FeedModel(final int id, final String title, final String imageUrl, final String pageLink, final String description, final String url) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
        this.pageLink = pageLink;
        this.description = description;
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getPageLink() {
        return pageLink;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }
}

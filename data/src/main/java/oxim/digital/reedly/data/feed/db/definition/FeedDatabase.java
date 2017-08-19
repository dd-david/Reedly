package oxim.digital.reedly.data.feed.db.definition;

import com.raizlabs.android.dbflow.annotation.Database;

@Database(name = FeedDatabase.NAME, version = FeedDatabase.VERSION)
public final class FeedDatabase {

    public static final String NAME = "FeedDatabase";

    public static final int VERSION = 1;
}

/*
*
* DBFlow : ORM(Object-relational mapper) Android Db
* https://github.com/Raizlabs/DBFlow
*
* Guideline : https://agrosner.gitbooks.io/dbflow/content/
*
* @Database(name = CLASS.NAME, version = CLASS.VERSION)
* public final class CLASS {
*   public static final String NAME = "name";
*   public static final int VERSION = 1;
* }
* DatabaseDefinition 을 생성합니다.
* NAME 으로 정의한것처럼 name.db 파일을 바라봅니다.
* 설정된 DatabaseDefinition 을 확인하고 싶으면 다음과 같이 확인할 수 있습니다
*   DatabaseDefinition db = FlowManager.getDatabase(AppDatabase.class);
*
* */
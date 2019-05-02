package sgs.ui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MainUI extends Stage {
	
	
	private Table main_table;
	private Skin skin;

	public MainUI(Skin skin, Viewport viewport) {
		super(viewport);
		this.skin = skin;
		buildUI();
	}

	public MainUI(Skin skin, Viewport viewport, Batch batch) {
		super(viewport, batch);
		this.skin = skin;
		buildUI();
	}
	
	private void buildUI() {
		main_table = new Table(skin);
		String ascii_art = 
				"                  __..-----')\n" + 
				"        ,.--._ .-'_..--...-'\n" + 
				"       '-\"'. _/_ /  ..--''\"\"'-.\n" + 
				"       _.--\"\"...:._:(_ ..:\"::. \\\n" + 
				"    .-' ..::--\"\"_(##)#)\"':. \\ \\)    \\ _|_ /\n" + 
				"   /_:-:'/  :__(##)##)    ): )   '-./'   '\\.-'\n" + 
				"   \"  / |  :' :/\"\"\\///)  /:.'    --(       )--\n" + 
				"     / :( :( :(   (#//)  \"       .-'\\.___./'-.\n" + 
				"    / :/|\\ :\\_:\\   \\#//\\            /  |  \\\n" + 
				"    |:/ | \"\"--':\\   (#//)              '\n" + 
				"    \\/  \\ :|  \\ :\\  (#//)\n" + 
				"         \\:\\   '.':. \\#//\\\n" + 
				"          ':|    \"--'(#///)\n" + 
				"                     (#///)\n" + 
				"                     (#///)         ___/\"\"\\     \n" + 
				"                      \\#///\\           oo##\n" + 
				"                      (##///)         `-6 #\n" + 
				"                      (##///)          ,'`.\n" + 
				"                      (##///)         // `.\\\n" + 
				"                      (##///)        ||o   \\\\\n" + 
				"                       \\##///\\        \\-+--//\n" + 
				"                       (###///)       :_|_(/\n" + 
				"                       (sjw////)__...--:: :...__\n" + 
				"                       (#/::'''        :: :     \"\"--.._\n" + 
				"                  __..-'''           __;: :            \"-._\n" + 
				"          __..--\"\"                  `---/ ;                '._\n" + 
				" ___..--\"\"                             `-'                    \"-..___\n" + 
				"\n" + 
				"   (_ \"\"---....___                                     __...--\"\" _)\n" + 
				"     \"\"\"--...  ___\"\"\"\"\"-----......._______......----\"\"\"     --\"\"\"\n" + 
				"                   \"\"\"\"       ---.....   ___....----\n"+
				"non so perche sta ascii art sfaciola...";
		main_table.add(ascii_art).expand();
		main_table.add("qua ci vanno grafici interfaccia e robe varie").expand();
		
		main_table.setFillParent(true);
		addActor(main_table);
		setDebugAll(true);
	}

}

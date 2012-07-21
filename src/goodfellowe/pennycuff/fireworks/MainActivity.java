package goodfellowe.pennycuff.fireworks;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.support.v4.app.NavUtils;
import android.view.View.OnClickListener;
import android.media.AudioManager;


public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
                
        final MediaPlayer starsMp = MediaPlayer.create(this,  R.raw.stars_and_stripes_forever);
        final MediaPlayer spangledMp = MediaPlayer.create(this,  R.raw.the_star_spangled_banner);
        final MediaPlayer thundererMp = MediaPlayer.create(this,  R.raw.thunderer);
        final MediaPlayer washingtonMp = MediaPlayer.create(this,  R.raw.washington_post);
                        
        Button stripesPlayerButton = (Button) this.findViewById(R.id.stripes);
        stripesPlayerButton.setVisibility(0);
        //stripesPlayerButton.setBackgroundColor(Color.TRANSPARENT);        
        stripesPlayerButton.setOnClickListener(new View.OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		if (starsMp.isPlaying() == true) {
        			starsMp.pause();
        			starsMp.seekTo(0);
        		}
        		else if (starsMp.isPlaying() == false)
        		{
        			
        			starsMp.start();
        		}
        	}
        });
        Button spangledPlayerButton = (Button) this.findViewById(R.id.spangled);
        spangledPlayerButton.setVisibility(0);
        spangledPlayerButton.setOnClickListener(new View.OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		if (spangledMp.isPlaying() == true) {
        			spangledMp.pause();
        			spangledMp.seekTo(0);
        		}
        		else if (spangledMp.isPlaying() == false)
        		{
        			
        			spangledMp.start();
        		}
        	}
        });
        Button washingtonPlayerButton = (Button) this.findViewById(R.id.washington);
        washingtonPlayerButton.setOnClickListener(new View.OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		if (washingtonMp.isPlaying() == true) {
        			washingtonMp.pause();
        			washingtonMp.seekTo(0);
        		}
        		else if (washingtonMp.isPlaying() == false)
        		{
        			
        			washingtonMp.start();
        		}
        	}
        });
        Button thundererPlayerButton = (Button) this.findViewById(R.id.thunderer);
        thundererPlayerButton.setOnClickListener(new View.OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		if (thundererMp.isPlaying() == true) {
        			thundererMp.pause();
        			thundererMp.seekTo(0);
        		}
        		else if (thundererMp.isPlaying() == false)
        		{
        			
        			thundererMp.start();
        		}
        	}
        });
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    
}

package raghu.project0;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static String message="This button will launch my";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("My app Portfolio");

    }

    public void buttonClick(View v)
    {
        switch (v.getId())
        {
            case R.id.b1:
                display(message + " " + getResources().getString(R.string.b1));
                break;
            case R.id.b2:
                display(message + " " + getResources().getString(R.string.b2));
                break;
            case R.id.b3:
                display(message + " " + getResources().getString(R.string.b3));
                break;
            case R.id.b4:
                display(message + " " + getResources().getString(R.string.b4));
                break;
            case R.id.b5:
                display(message + " " + getResources().getString(R.string.b5));
                break;
            case R.id.b6:
                display(message + " " + getResources().getString(R.string.b6));
                break;

        }
    }

    private void display(String mess)
    {
        Toast.makeText(getApplicationContext(),mess,Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

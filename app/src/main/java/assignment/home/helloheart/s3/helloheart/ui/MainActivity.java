package assignment.home.helloheart.s3.helloheart.ui;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import assignment.home.helloheart.s3.helloheart.R;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HelloHeartFragment regionsFragment = HelloHeartFragment.newInstance();
        openFragment(regionsFragment);

    }

    private void openFragment(HelloHeartFragment fragment) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction().replace(R.id.main_layout, fragment);
        fragmentTransaction.addToBackStack(fragment.getClass().getName());
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {

        FragmentManager fm = getFragmentManager();

        if (fm.getBackStackEntryCount() > 1) {
            fm.popBackStack();
        }
        else {
            finish();
        }

    }
}

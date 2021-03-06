package com.example.arshad.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DetailsActivity extends ActionBarActivity {


    static ArrayList<Movie> mvi = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
            mvi = this.getIntent().getParcelableArrayListExtra("movie");
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings){
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        private static final String LOG_TAG = PlaceholderFragment.class.getSimpleName();

        public PlaceholderFragment() {
            setHasOptionsMenu(true);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            Intent intent = getActivity().getIntent();
            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
            if (intent != null){
                ((TextView) rootView.findViewById(R.id.details_title)).setText(mvi.get(0).getTitle());
                Picasso.with(rootView.getContext()).load(mvi.get(0).getPoster()).into((ImageView) rootView.findViewById(R.id.details_image));
                ((TextView) rootView.findViewById(R.id.details_overviewDetails)).setText(mvi.get(0).getOverview());
                ((TextView) rootView.findViewById(R.id.details_ratingDetail)).setText(mvi.get(0).getRating());
                ((TextView) rootView.findViewById(R.id.details_reldateDetail)).setText(mvi.get(0).getDate());
            }
            return rootView;
        }

//        @Override
//        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//            inflater.inflate(R.menu.detailfragment, menu);
//
//            //get share menu item
//            MenuItem menuItem = menu.findItem(R.id.action_share);
//            //get provider and work
//            ShareActionProvider mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
//            if (mShareActionProvider !=null){
//                mShareActionProvider.setShareIntent(createShareIntent());
//            }else {
//                Log.d(LOG_TAG, "no share action provider");
//            }
//        }
//
//        private Intent createShareIntent(){
//            Intent shareIntent = new Intent(Intent.ACTION_SEND);
//            shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
//            shareIntent.setType("text/plain");
//            shareIntent.putExtra(Intent.EXTRA_TEXT, mForecastStr+share_hashtag);
//            return shareIntent;
//        }
    }



}


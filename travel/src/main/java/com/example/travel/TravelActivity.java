package com.example.travel;

import android.app.Activity;
import android.app.ListFragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.Collections;

public class TravelActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new TravelFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.travel, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class TravelFragment extends ListFragment {

        private Travel mTravel;
        private ProgressDialog mProgressDialog;

        public TravelFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            try {
                mTravel = new Travel(getActivity());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            }

            final View rootView = inflater.inflate(R.layout.fragment_travel, container, false);
            BoardingCardListAdapter adapter = new BoardingCardListAdapter(getActivity(), mTravel.boardingCards);
            setListAdapter(adapter);

            Button sortButton = (Button) rootView.findViewById(R.id.sort_button);
            sortButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    // Use an AsyncTask to display a progress dialog to the user and add in a
                    // sleep so that we can see it
                    AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {

                        @Override
                        protected void onPreExecute() {
                            mProgressDialog = new ProgressDialog(getActivity());
                            mProgressDialog.setTitle("Sorting...");
                            mProgressDialog.setMessage("Please wait.");
                            mProgressDialog.setCancelable(false);
                            mProgressDialog.setIndeterminate(true);
                            mProgressDialog.show();
                        }

                        @Override
                        protected Void doInBackground(Void... arg0) {
                            try {
                                Collections.sort(mTravel.boardingCards);
                                // It doesn't really take very long, so adding in a sleep to
                                // show the progress dialog
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void result) {
                            if (mProgressDialog != null) {
                                // Update the views on the UI thread to reflect the new order
                                rootView.findViewById(R.id.sort_button).setVisibility(View.GONE);
                                ((TextView) rootView.findViewById(R.id.title)).setText(R.string.sorted_trip);
                                ((BoardingCardListAdapter) getListAdapter()).notifyDataSetChanged();
                                mProgressDialog.dismiss();
                            }
                        }

                    };
                    task.execute((Void[])null);
                }
            });
            return rootView;
        }
    }

}

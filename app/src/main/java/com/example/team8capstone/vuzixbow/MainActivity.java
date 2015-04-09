package com.example.team8capstone.vuzixbow;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v13.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.team8capstone.vuzixbow.video.VideoActivity;

public class MainActivity extends Activity implements RecognitionListener {
    // Array of Card Infos
    private static ArrayList<CardInfo> cardInfos = new ArrayList<CardInfo>();

    SpeechRecognizer mSpeechRecognizer;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v13.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    private Intent video;

    private boolean playing = false;

    AlertDialog.Builder builder1;

    AlertDialog alert11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(AUDIO_SERVICE,"onCreate");

        setCardInfo();

        setContentView(R.layout.activity_main);


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

//        mSpeechRecognizer = getSpeechRecognizer();

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
                getPackageName());

        getSpeechRecognizer().startListening(intent);

        // Instantiates a new intent for the VideoActivity that will be activated when
        // the user wishes to view a video
        video = new Intent(this, VideoActivity.class);

        setPageSelectedListener();
        setMediaResources(0);

        builder1 = new AlertDialog.Builder(MainActivity.this);

        builder1.setMessage("Repeat Shut Down to Exit");
        builder1.setCancelable(true);
        builder1.setPositiveButton("Shut Down",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
        builder1.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        alert11 = builder1.create();
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        Log.v(AUDIO_SERVICE,"onResume");
//        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
//                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
//
//        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
//                getPackageName());
//
//        getSpeechRecognizer().startListening(intent);
//
//    }

//    @Override
//    protected void onPause() {
//        Log.v(AUDIO_SERVICE,"onPause");
//        if(getSpeechRecognizer()!=null){
//            getSpeechRecognizer().stopListening();
//            getSpeechRecognizer().cancel();
////            getSpeechRecognizer().destroy();
//        }
////        mSpeechRecognizer = null;
//
//        super.onPause();
//    }

    @Override
    public void onDestroy(){
        Log.v(AUDIO_SERVICE,"onDestroy");
        if(mSpeechRecognizer!=null){
            mSpeechRecognizer.stopListening();
            mSpeechRecognizer.cancel();
//            mSpeechRecognizer.destroy();
        }
//        mSpeechRecognizer = null;

        super.onDestroy();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            Toast.makeText(this, "Selected.", Toast.LENGTH_SHORT).show();
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public void onReadyForSpeech(Bundle params) {
//        Toast.makeText(this, "Voice Recognition Ready.", Toast.LENGTH_SHORT).show();
        //iv.setImageResource(R.drawable.ready);
    }

    @Override
    public void onBeginningOfSpeech() {
//        Toast.makeText(this, "Please speak command.", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onRmsChanged(float rmsdB) {
        Log.v(AUDIO_SERVICE,"recieve : " + rmsdB + "dB");
    }

    @Override
    public void onBufferReceived(byte[] buffer) {
        Log.v(AUDIO_SERVICE,"recieved");
    }

    @Override
    public void onEndOfSpeech() {
        Log.v(AUDIO_SERVICE,"finished.");
    }

    @Override
    public void onError(int error) {
        Log.v(AUDIO_SERVICE,"Error: " + error);
        finish();
    }

    @Override
    public void onResults(Bundle results) {
        // Vuzix M100 v1.0.8 only can support 'onPartialResuts'
        Log.v(AUDIO_SERVICE,"onResults called");

    }

    @Override
    public void onPartialResults(Bundle partialResults) {
        // Vuzix M100 v1.0.8 only can support 'onPartialResuts'
        // Supported keywords:
        //   move left/right/up/down
        //   go left/right/up/down
        //   left/right/up/down
        //   next, previous, forward, back
        //   select, cancel
        //   complete, stop, exit, go home
        //   menu, volume up/down
        //   mute, call, dial, hang up, answer
        //   ignore, end, redial, call back
        //   contacts, favorites, pair, unpair
        //   sleep, shut down
        //   set clock/time
        //   cut, copy, paste, delete
        //   0, 1, 2, 3, 4, 5, 6, 7, 8, 9
        String[] keywords = new String[]{"next", "previous", "select", "stop", "cancel", "exit", "shut down"};
        ArrayList<String> recData = partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        String getData = new String();



        for (String s : recData) {
            getData += s + ",";
        }

        // Show filtered keyword
        String result = "";
        for (String s: recData){
            for (String t: keywords){
                if(s.equals(t)){
                    result = t;
                    break;
                }
            }
            if(!result.isEmpty()){
//                Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
                switch (result) {
                    case "next":
                        if (mViewPager.getCurrentItem() < cardInfos.size()) {
                            mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1, true);
                        }
                        break;
                    case "previous":
                        if (mViewPager.getCurrentItem() > 0) {
                            mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1, true);
                        }
                        break;
                    case "select":
                        if (!playing) {
                            startMedia(mViewPager.getCurrentItem());
                            playing = true;
                        }
                        break;
                    case "stop":
                        if (playing) {
                            Intent i = new Intent(MainActivity.this, VideoActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            i.putExtra("finish",true);
                            MainActivity.this.startActivity(i);
//                            playing = false;
                        }
                        break;
                    case "cancel":
                        if (alert11.isShowing()) {
                            alert11.getButton(AlertDialog.BUTTON_NEGATIVE).performClick();
                        }
                        break;
                    case "shut down":
                        if (!playing) {
                            if (!alert11.isShowing()) {
                                alert11.show();
                            }
                            else {
                                alert11.getButton(AlertDialog.BUTTON_POSITIVE).performClick();
                            }
                        }
                        break;
                    default:
                        break;
                }
                result = "";
            }
        }
        // Raw Result
        Log.v(AUDIO_SERVICE, "Result: " + getData);
    }


    @Override
    public void onEvent(int eventType, Bundle params) {
        Log.v(AUDIO_SERVICE,"onEvent called");
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position);


        }

        @Override
        public int getCount() {
            return 11;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            int position = (int) getArguments().get(ARG_SECTION_NUMBER);

            View rootView = inflater.inflate(cardInfos.get(position).xmlLayout, container, false);

            ViewGroup rightColumn = (ViewGroup) rootView.findViewById(R.id.right_column);

            if (cardInfos.get(position).hasImage){
                ImageView imageView = (ImageView) rightColumn.findViewById(R.id.image);
                imageView.setImageResource(cardInfos.get(position).imageResource);
            }

            ViewGroup leftColumn = (ViewGroup) rootView.findViewById(R.id.left_column);

            if (cardInfos.get(position).hasHeader) {
                TextView textViewHeader = (TextView) leftColumn.findViewById(R.id.header);
                textViewHeader.setText(cardInfos.get(position).header);
                textViewHeader.setTextSize(cardInfos.get(position).headerTextSize);
            }

            if (cardInfos.get(position).hasText) {
                TextView textViewContent = (TextView) leftColumn.findViewById(R.id.content);
                textViewContent.setText(cardInfos.get(position).text);
                textViewContent.setTextSize(cardInfos.get(position).textSize);
            }
            return rootView;
        }
    }

    private SpeechRecognizer getSpeechRecognizer() {
        if(mSpeechRecognizer == null){
            mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(MainActivity.this);
            mSpeechRecognizer.setRecognitionListener(MainActivity.this);
        }
        return mSpeechRecognizer;
    }

    // Set Card Info
    private void setCardInfo() {
        cardInfos.add(new CardInfo(cardInfos.size(), R.layout.fragment_main)
                .setHeader("Getting to know the bow tie")
                .addBullet("Both sides are the same size")
                .addBullet("The length is adjustable on the inside of the bow tie")
                .addBullet("Adjust the length to your neck size, adding a ½ inch may be helpful while learning")
                .setVideoResource(R.raw.bvid1)
                .setImageResource(R.drawable.bpic1));

        cardInfos.add(new CardInfo(cardInfos.size(), R.layout.fragment_main)
                .setHeader("Tie Orientation")
                .addBullet("Place the tie with the inside against your neck")
                .addBullet("The Right Side should be about 1 ½ “ longer than the Left.")
                .setVideoResource(R.raw.bvid2)
                .setImageResource(R.drawable.bpic2));

        cardInfos.add(new CardInfo(cardInfos.size(), R.layout.fragment_main)
                .setHeader("The Cross Over")
                .addBullet("Take the long side and cross it over the front of the short side")
                .addBullet("Then slip the long side behind the short side and up towards your face")
                .addBullet("This will look similar to tying a shoe")
                .addBullet("Keep this snug against your neck")
                .setVideoResource(R.raw.bvid3)
                .setImageResource(R.drawable.bpic3));

        cardInfos.add(new CardInfo(cardInfos.size(), R.layout.fragment_main)
                .setHeader("The First Bow")
                .addBullet("The bottom end forms the first bow")
                .addBullet("Pinch the narrowest portion of the bottom end with your right hand")
                .addBullet("Bring the your pinched tie to the middle of the cross over")
                .addBullet("The bow should form with a fold on the left side")
                .setVideoResource(R.raw.bvid4)
                .setImageResource(R.drawable.bpic4)
                .setTextSize(18));

        cardInfos.add(new CardInfo(cardInfos.size(), R.layout.fragment_main)
                .setHeader("Middle Stripe")
                .addBullet("Pinch First bow and Knot with right hand")
                .addBullet("Lower top side in front of the first bow")
                .addBullet("Make sure the tie remains flat in front of the bow")
                .addBullet("This creates a middle stripe")
                .setVideoResource(R.raw.bvid5)
                .setImageResource(R.drawable.bpic5));

        cardInfos.add(new CardInfo(cardInfos.size(), R.layout.fragment_main)
                .setHeader("Pinch and pull")
                .addBullet("Pinch the two ends of the first bow, surrounding the middle stripe with your right hand")
                .addBullet("Pull this forward, and notice the Back Loop is formed behind the bow")
                .setVideoResource(R.raw.bvid6)
                .setImageResource(R.drawable.bpic6));

        cardInfos.add(new CardInfo(cardInfos.size(), R.layout.fragment_main)
                .setHeader("Back Loop")
                .addBullet("This is where the second bow will go through")
                .addBullet("The size of the loop is adjustable by loosening the middle stripe")
                .addBullet("This loop will be tightened later")
                .setVideoResource(R.raw.bvid7)
                .setImageResource(R.drawable.bpic7));

        cardInfos.add(new CardInfo(cardInfos.size(), R.layout.fragment_main)
                .setHeader("Start Second Bow ")
                .addBullet("Pinch bottom portion, where the tie is widest, with your left hand")
                .addBullet("Push pinched bow through Back Loop")
                .addBullet("The tie should naturally wrap your left pointer finger as you make this motion")
                .setVideoResource(R.raw.bvid8)
                .setImageResource(R.drawable.bpic8));

        cardInfos.add(new CardInfo(cardInfos.size(), R.layout.fragment_main)
                .setHeader("Finish Second Bow ")
                .addBullet("Carefully pull the folded portion of the tie through the Back loop")
                .addBullet("Be careful not to pull the free end through the loop too")
                .addBullet("The tie can now be adjusted for looks")
                .setVideoResource(R.raw.bvid9)
                .setImageResource(R.drawable.bpic9));

        cardInfos.add(new CardInfo(cardInfos.size(), R.layout.fragment_main)
                .setHeader("Adjusting the Bow")
                .addBullet("Carefully pull on the folded ends of the bow to tighten the Middle Stripe")
                .addBullet("Then adjust both the folded and free ends to make a symmetrical bow")
                .setVideoResource(R.raw.bvid10)
                .setImageResource(R.drawable.bpic10));

        cardInfos.add(new CardInfo(cardInfos.size(), R.layout.fragment_main)
                .setHeader("Common Problems")
                .addBullet("Second bow is too small")
                .addBullet("Tie is loose around neck")
                .addBullet("Often  wrong tie length or too loose of a knot")
                .setVideoResource(R.raw.bvid11)
                .setImageResource(R.drawable.bpic11));
    }

    // Set media resources based on slide position
    private void setMediaResources(int position){
        if (cardInfos.get(position).hasVideo){
            video.removeExtra("videoResource");
            video.putExtra("videoResource", cardInfos.get(position).videoResource);
        }
    }

    // Starts audio and video when card is selected
    private void startMedia(int position) {
        if (cardInfos.get(position).hasVideo) {
            startActivityForResult(video,1);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == RESULT_OK){
                playing = false;
            }
            if (resultCode == RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }//onActivityResult

    private void setPageSelectedListener() {
        mViewPager.setOnPageChangeListener( new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                setMediaResources(position);
            }
        });

        mViewPager.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!playing){
                    startMedia(mViewPager.getCurrentItem());
                    playing = true;
                }
            }
        });
    }





}

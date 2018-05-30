package assignment.home.helloheart.s3.helloheart.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import assignment.home.helloheart.s3.helloheart.R;
import assignment.home.helloheart.s3.helloheart.data.BloodTestConfigData;
import assignment.home.helloheart.s3.helloheart.network.NetworkManager;

/**
 * Created by AmirG on 5/28/2018.
 */

public class HelloHeartFragment extends Fragment implements NetworkManager.NetworkManagerListener {

    private static final String USER_FILTER = "[^0-9|^A-Z|^a-z]+";
    private static final String SERVER_FILTER = "\\s+";
    private static final double MINIMUM_SUTIABLE_CHARS_IN_PERCENTAGE = 0.6;

    private BloodTestConfigData serverData;
    private EditText testNameEditText;
    private EditText resultEditText;
    private TextView testResultTextView;

    public HelloHeartFragment() {
        // Required empty public constructor
    }

    public static HelloHeartFragment newInstance() {
        HelloHeartFragment fragment = new HelloHeartFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.am_i_ok_layout, container, false);

        testNameEditText = (EditText) view.findViewById(R.id.test_name_editText);
        testNameEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    handleUserPressedDoneButton();
                }
                return false;
            }
        });

        resultEditText = (EditText) view.findViewById(R.id.result_editText);
        resultEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    handleUserPressedDoneButton();
                }
                return false;
            }
        });

        testResultTextView = (TextView) view.findViewById(R.id.test_result_textView);


        NetworkManager.getInstance().setNetworkManagerListener(this);
        NetworkManager.getInstance().sendNetworkStatusToServer();

        return view;
    }

    private void handleUserPressedDoneButton() {

        testResultTextView.setText("");

        if(serverData != null) {
            BloodTestConfigData.Details selectedDetailItemFromServer = getItemThatCloseToUserInput();

            handleFinishToChooseItemFromList(selectedDetailItemFromServer);
        }
    }

    @Nullable
    /**
     * search for most suitable item to user input
     */
    private BloodTestConfigData.Details getItemThatCloseToUserInput() {
        String userInput = testNameEditText.getText().toString();

        String[] userInputDividedArray = userInput.split(USER_FILTER);

        List<BloodTestConfigData.Details> bloodTestConfig = serverData.getBloodTestConfig();

        BloodTestConfigData.Details selectedDetailItemFromServer = null;

        double suitableCharsInPercentage = 0;

        //search in all items from server array
        for (BloodTestConfigData.Details item: bloodTestConfig) {
            String detailNameFromServer = item.getName();
            String[] detailNameFromServerDividedArray = detailNameFromServer.split(SERVER_FILTER);
            double suitableCharsInPercentageItem = 0;
            double suitableCharsInPercentageSum = 0;
            double suitableCharsInPercentageTempItem = 0;

            BloodTestConfigData.Details selectedDetail;
            boolean didFindAtLeastOneWord = false;

            //search in every word of user divided array
            for (String userInputItem : userInputDividedArray)
            {
                int userInputItemLength = userInputItem.length();
                suitableCharsInPercentageItem = 0;

                //search in every word of every item from server array
                for (String detailNameFromServerItem : detailNameFromServerDividedArray)
                {
                    //calculate how much chars same in two strings and return in percentage
                    suitableCharsInPercentageTempItem = calculateSuitableCharsInItemInPercentage(userInputItem, detailNameFromServerItem);

                    //if found new item that better than best temporary item
                    if(suitableCharsInPercentageTempItem > suitableCharsInPercentageItem) {
                        suitableCharsInPercentageItem = suitableCharsInPercentageTempItem;

                        //if at least one word same
                        if(suitableCharsInPercentageTempItem == 1) {
                            didFindAtLeastOneWord = true;
                        }
                    }
                }

                suitableCharsInPercentageSum = suitableCharsInPercentageSum + suitableCharsInPercentageItem;
            }

            double suitableCharsInPercentageTemp = (suitableCharsInPercentageSum / (double) userInputDividedArray.length);

            //if new item from server bigger than current selected item AND (at least MINIMUM_SUTIABLE_CHARS_IN_PERCENTAGE success OR at least one word exists we will save it to
            //current best result)
            if(suitableCharsInPercentageTemp > suitableCharsInPercentage && (suitableCharsInPercentageTemp >= MINIMUM_SUTIABLE_CHARS_IN_PERCENTAGE || didFindAtLeastOneWord)) {
                suitableCharsInPercentage = suitableCharsInPercentageTemp;
                selectedDetailItemFromServer = item;

                //if all input user entered same to one of the items we can stop searching
                if(suitableCharsInPercentage == 1) {
                    break;
                }
            }
        }
        return selectedDetailItemFromServer;
    }

    /**
     * calculate how much chars same in two strings
     * @param userInputItem
     * @param detailNameFromServerItem
     * @return number of success in percentage
     */
    private double calculateSuitableCharsInItemInPercentage(String userInputItem, String detailNameFromServerItem)
    {
        double countSuitableChars = 0;
        double suitableCharsInPercentage = 0;
        double lengthOfBiggerString = 0;
        double lengthOfSmallerString = 0;

        if(userInputItem.length() > detailNameFromServerItem.length()) {
            lengthOfBiggerString = userInputItem.length();
            lengthOfSmallerString = detailNameFromServerItem.length();
        }
        else {
            lengthOfSmallerString = userInputItem.length();
            lengthOfBiggerString = detailNameFromServerItem.length();
        }

        for( int i = 0; i < lengthOfSmallerString; i++) {
            double suitableCharsInPercentageTemp = 0;
            if(Character.toLowerCase(userInputItem.charAt(i)) == Character.toLowerCase(detailNameFromServerItem.charAt(i))) {
                countSuitableChars++;
            }

            suitableCharsInPercentageTemp = countSuitableChars / lengthOfBiggerString;

            if(suitableCharsInPercentageTemp > suitableCharsInPercentage) {
                suitableCharsInPercentage = suitableCharsInPercentageTemp;
            }

        }

        return suitableCharsInPercentage;

    }

    private void handleFinishToChooseItemFromList (BloodTestConfigData.Details selectedDetailItemFromServer) {

        StringBuilder resultToPrint = new StringBuilder();

        //found item from server
        if(selectedDetailItemFromServer != null) {
            handleFoundItemFromList(selectedDetailItemFromServer, resultToPrint);
        }

        //didn't find item from server
        else {
            handleDidntFindItemFromServer(resultToPrint);
        }

        testResultTextView.setText(resultToPrint.toString());
    }

    private void handleFoundItemFromList(BloodTestConfigData.Details selectedDetailItemFromServer, StringBuilder resultToPrint) {

        if(isInteger(resultEditText.getText().toString())) {

            resultToPrint.append(selectedDetailItemFromServer.getName() + " - ");

            int result = Integer.parseInt(resultEditText.getText().toString());

            if (result < selectedDetailItemFromServer.getThreshold()) {
                resultToPrint.append(getString(R.string.blood_test_category_good));
            }
            else {
                resultToPrint.append(getString(R.string.blood_test_category_bad));
            }
        }
    }

    private void handleDidntFindItemFromServer(StringBuilder resultToPrint) {
        resultToPrint.append(getString(R.string.blood_test_category_unknown));
    }

    public boolean isInteger(String string) {
        try {
            Integer.valueOf(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public void onResponseFromServerSucceeded(BloodTestConfigData serverData) {
        this.serverData = serverData;
    }

    @Override
    public void onResponseFromServerFailed() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        NetworkManager.getInstance().setNetworkManagerListener(null);
    }
}

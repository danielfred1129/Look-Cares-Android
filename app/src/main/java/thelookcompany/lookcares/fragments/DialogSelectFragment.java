package thelookcompany.lookcares.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import thelookcompany.lookcares.Adapters.DialogListAdapter;
import thelookcompany.lookcares.R;
import thelookcompany.lookcares.datamodel.DialogListItem;
import thelookcompany.lookcares.datamodel.UserObject;
import thelookcompany.lookcares.network.LookCaresResponseHandler;
import thelookcompany.lookcares.utils.UserUtils;
import thelookcompany.lookcares.utils.Utils;

public class DialogSelectFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_TYPE = "type";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mType;
    private String mParam2;

    private View rootView;
    private DialogListAdapter adapter;

    int selectedID = -1;

    private JSONArray resultArray;

    public interface onSelectFragmentListener {
        public void onClientSelection(String s);
    }

    onSelectFragmentListener selectFragmentListener;


    public DialogSelectFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static DialogSelectFragment newInstance(String type, String param2) {
        DialogSelectFragment fragment = new DialogSelectFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TYPE, type);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mType = getArguments().getString(ARG_TYPE);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_dialog_select, container, false);
        TextView lbl_dialog_select_title = (TextView)rootView.findViewById(R.id.lbl_dialog_select_title);
        if (mType.equals("client"))
            lbl_dialog_select_title.setText("Select Client");
        else if (mType.equals(("location")))
            lbl_dialog_select_title.setText("Select Location");
        else if (mType.equals("in-store-location"))
            lbl_dialog_select_title.setText("Select In-Store Location");

        Button btn_cancel_dialog = (Button) rootView.findViewById(R.id.btn_cancel_dialog);
        btn_cancel_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        Button btn_select_dialog = (Button) rootView.findViewById(R.id.btn_select_dialog);
        btn_select_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedID == -1) {
                    Utils.showAlertWithTitleNoCancel(getActivity(), "Warning", "Please select " + mType);
                    return;
                }
                if (mType.equals("client"))
                {
                    try {
                        String selectedClient = resultArray.getJSONObject(selectedID).toString();
                        ((Button)getActivity().findViewById(R.id.btn_client)).setText(resultArray.getJSONObject(selectedID).getString("vcClientName"));
                        UserUtils.storeSelectedClient(getActivity(), selectedClient);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else if (mType.equals("location"))
                {
                    try {
                        String selectedLocation = resultArray.getJSONObject(selectedID).toString();
                        JSONObject object = resultArray.getJSONObject(selectedID);
                        String vcShipToStateProvince = object.getString("vcShipToStateProvince");
                        String vcShipToPostal = object.getString("vcShipToPostal");
                        String vcShipToName = object.getString("vcShipToName");
                        String vcShipToCountry = object.getString("vcShipToCountry");
                        String vcShipToContact = object.getString("vcShipToContact");
                        String vcShipToCity = object.getString("vcShipToCity");
                        String vcShipToAddress1 = object.getString("vcShipToAddress1");
                        String location = vcShipToStateProvince + " " + vcShipToPostal + " " + vcShipToName
                                + " " + vcShipToCountry + " " + vcShipToContact + " " + vcShipToCity + " " + vcShipToAddress1;

                        ((Button)getActivity().findViewById(R.id.btn_location)).setText(location);
                        UserUtils.storeSelectedLocation(getActivity(), selectedLocation);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else if (mType.equals("in-store-location"))
                {
                    try {
                        String selectedStoreLocation = resultArray.getJSONObject(selectedID).toString();
                        ((Button)getActivity().findViewById(R.id.btn_store_location)).setText(resultArray.getJSONObject(selectedID).getString("vcInStoreLocation"));
                        UserUtils.storeSelectedStoreLocation(getActivity(), selectedStoreLocation);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        ListView listview_dialog = (ListView) rootView.findViewById(R.id.listview_dialog);
        adapter = new DialogListAdapter(new ArrayList<DialogListItem>());
        listview_dialog.setAdapter(adapter);
        listview_dialog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
//                view.setSelected(true);
//                view.setBackgroundColor(Color.rgb(242,242,242));
                DialogListItem item = (DialogListItem) adapter.getItem(position);
                selectedID = position;
            }
        });
        initializeControl();
        return rootView;
    }
    private void initializeControl() {
        getListInformation();
    }
    private void getAllClients() {
        RequestParams params = new RequestParams();
        UserObject user = UserUtils.getSession(getActivity());
        String token = user.getToken();

        AsyncHttpClient client = new AsyncHttpClient();
        String authorization = "base " + token;
        client.addHeader("Authorization", authorization);
        client.addHeader("Content-Type", "application/json");
        client.addHeader("Accept", "application/json");
        client.get(Utils.BASE_URL + "Clients",new LookCaresResponseHandler(getActivity()) {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);

                if (response != null) {
                    try {
                        resultArray = response;
                        adapter.clearData();
                        if (resultArray.length() > 0) {
                            for (int i = 0; i < resultArray.length(); i++) {
                                DialogListItem item = new DialogListItem();
                                JSONObject object = resultArray.getJSONObject(i);
                                String vcClientName = object.getString("vcClientName");
                                item.address = vcClientName;
                                item.img = mType;
                                adapter.addData(item);
                            }
                        }
                    } catch (JSONException e) {
                    }
                }
            }
        });
    }

    private void getLocationsWithClient(String clientKey) {
        RequestParams params = new RequestParams();
        UserObject user = UserUtils.getSession(getActivity());
        String token = user.getToken();

        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Authorization", "base " + token);

        client.get(Utils.BASE_URL + "ClientLocations/ByClientKey/" + clientKey,new LookCaresResponseHandler(getActivity()) {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);

                if (response != null) {
                    try {
                        resultArray = response;
                        adapter.clearData();
                        if (resultArray.length() > 0) {
                            for (int i = 0; i < resultArray.length(); i++) {
                                DialogListItem item = new DialogListItem();
                                JSONObject object = resultArray.getJSONObject(i);
                                String vcShipToStateProvince = object.getString("vcShipToStateProvince");
                                String vcShipToPostal = object.getString("vcShipToPostal");
                                String vcShipToName = object.getString("vcShipToName");
                                String vcShipToCountry = object.getString("vcShipToCountry");
                                String vcShipToContact = object.getString("vcShipToContact");
                                String vcShipToCity = object.getString("vcShipToCity");
                                String vcShipToAddress1 = object.getString("vcShipToAddress1");
                                item.address = vcShipToStateProvince + " " + vcShipToPostal + " " + vcShipToName
                                        + " " + vcShipToCountry + " " + vcShipToContact + " " + vcShipToCity + " " + vcShipToAddress1;
                                item.img = mType;
                                adapter.addData(item);
                            }
                        }
                    } catch (JSONException e) {
                    }
                }
            }
        });
    }
    private void getStoreLocations() {
        RequestParams params = new RequestParams();
        UserObject user = UserUtils.getSession(getActivity());
        String token = user.getToken();

        AsyncHttpClient client = new AsyncHttpClient();
        String authorization = "base " + token;
        client.addHeader("Authorization", authorization);
        client.addHeader("Content-Type", "application/json");
        client.addHeader("Accept", "application/json");
        client.get(Utils.BASE_URL + "StoreLocations",new LookCaresResponseHandler(getActivity()) {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);

                if (response != null) {
                    try {
                        adapter.clearData();
                        resultArray = response;
                        if (response.length() > 0) {
                            for (int i = 0; i < response.length(); i++) {
                                DialogListItem item = new DialogListItem();
                                JSONObject object = resultArray.getJSONObject(i);
                                String vcInStoreLocation = object.getString("vcInStoreLocation");
                                item.address = vcInStoreLocation;
                                item.img = mType;
                                adapter.addData(item);
                            }
                        }
                    } catch (JSONException e) {
                    }
                }
            }
        });
    }
    private void getListInformation() {
        if (mType.equals("client")) { //if select client
            getAllClients();
        }
        else if (mType.equals("location")) //if select location
        {
            String selectedClient = UserUtils.getSelectedClient(getActivity());
            try {
                JSONObject json_client = new JSONObject(selectedClient);
                String selectedClientKey = json_client.getString("kLookClient");
                getLocationsWithClient(selectedClientKey);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if (mType.equals("in-store-location")) // if select in-store-location
        {
            getStoreLocations();
        }
    }

}

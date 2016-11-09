package thelookcompany.lookcares.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

import thelookcompany.lookcares.Adapters.DialogListAdapter;
import thelookcompany.lookcares.R;
import thelookcompany.lookcares.datamodel.DialogListItem;

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
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        ListView listview_dialog = (ListView) rootView.findViewById(R.id.listview_dialog);
        adapter = new DialogListAdapter(new ArrayList<DialogListItem>());
        listview_dialog.setAdapter(adapter);
        listview_dialog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                view.setSelected(true);
                view.setBackgroundColor(Color.rgb(242,242,242));
                DialogListItem item = (DialogListItem) adapter.getItem(position);

            }
        });
        initializeControl();
        return rootView;
    }
    private void initializeControl() {
        getListInformation();
    }
    private void getListInformation() {
        for (int i = 0; i < 10; i++)
        {
            DialogListItem item = new DialogListItem();
            item.address = "abc";
            item.img = mType;
            adapter.addData(item);
        }
    }

}

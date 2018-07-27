package com.nsappsstudio.shravanimela;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ContactFrag.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ContactFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContactFrag extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View v;
    private Context c;

    private OnFragmentInteractionListener mListener;
    private String fileName;
    private RecyclerView recyclerView;

    public ContactFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ContactFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static ContactFrag newInstance(String param1, String param2) {
        ContactFrag fragment = new ContactFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_contact,container, false);
        fileName = getArguments().getString("filename");




        recyclerView=v.findViewById(R.id.contact_recylerview);
        c = getContext();
        loadTable();

        return v;       }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public String loadJSONFromAsset() {
        SharedPreferences sharedPref = Objects.requireNonNull(this.getActivity()).getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String language= sharedPref.getString("lang",null);
        if(language!=null && language.equals("en")){
            String engFile=null;
            switch (fileName){

                case "Bhagalpur Contact.json":
                    engFile = "Bhagalpur Contact_eng.json";
                    break;
                case "Munger Contact.json":
                    engFile="Munger Contact_eng.json";
                    break;
                case "Banka Contact.json":
                    engFile="Banka Contact_eng.json";
                    break;
            }
                String json ;
                try {
                    InputStream is = c.getAssets().open(engFile);
                    int size = is.available();
                    byte[] buffer = new byte[size];
                    is.read(buffer);
                    is.close();
                    json = new String(buffer, "UTF-8");
                } catch (IOException ex) {
                    ex.printStackTrace();
                    return null;
                }
                return json;

        }else {

            String json;
            try {
                InputStream is = c.getAssets().open(fileName);
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                json = new String(buffer, "UTF-8");
            } catch (IOException ex) {
                ex.printStackTrace();
                return null;
            }
            return json;
        }
    }

    private void loadTable(){
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(c));
        List<TableList> tableLists = new ArrayList<>();

        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            JSONArray m_jArray = obj.getJSONArray("table");

            for (int i = 0; i < m_jArray.length(); i++) {

                JSONObject jo_inside = m_jArray.getJSONObject(i);
                String cell1 = jo_inside.getString("cell1");
                String cell2 = jo_inside.getString("cell2");
                String cell3 = jo_inside.getString("cell3");
                boolean title;
                boolean even;

                if (i==0){
                    title=true;
                    even=false;
                }else {
                    title=false;
                    if ((i % 2) == 0) {
                        // number is even
                        even=true;
                    }

                    else {
                        // number is odd
                        even=false;
                    }
                }
                TableList tableList= new TableList(title,null,cell1,cell2,cell3 ,null,even,3);
                tableLists.add(tableList);

                RecyclerView.Adapter adapter = new TableAdapter(tableLists, c);
                recyclerView.setAdapter(adapter);
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }

    }

}

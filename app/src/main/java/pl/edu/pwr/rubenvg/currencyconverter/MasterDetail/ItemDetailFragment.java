package pl.edu.pwr.rubenvg.currencyconverter.MasterDetail;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import pl.edu.pwr.rubenvg.currencyconverter.Database.Currency;
import pl.edu.pwr.rubenvg.currencyconverter.R;


/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private Currency mItem;
    private ImageView graph;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(ARG_ITEM_ID)) {
            mItem = ContentList.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.getName());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.item_detail, container, false);
        if (mItem != null) {
            graph = (ImageView) rootView.findViewById(R.id.grafica);
            String url = "http://themoneyconverter.com/exchange-rate-chart/EUR/EUR-"+mItem.getCode()+".gif";
            //placeholder(R.color.colorPrimaryDark) -> The image is too heavy, so it was only loading a
            //thumbnail instead of the full image
            Glide.with(this).load(url).placeholder(R.color.white).into(graph);
            Resources res = getResources();
            ((TextView) rootView.findViewById(R.id.country)).setText(mItem.getCountry().replace("\n",""));
            ((TextView) rootView.findViewById(R.id.region)).setText(mItem.getRegion().replace("\n",""));
            ((TextView) rootView.findViewById(R.id.subUnit)).setText(mItem.getSubUnit().replace("\n",""));
            ((TextView) rootView.findViewById(R.id.description)).setText(mItem.getDescription().replace("\n",""));
            ((TextView) rootView.findViewById(R.id.symbol)).setText(mItem.getSymbol().replace("\n",""));
            String flag = mItem.getCode().toLowerCase() + "_flag";
            int resID = res.getIdentifier(flag, "drawable", getActivity().getPackageName());
            Drawable drawable = res.getDrawable(resID);
            ((ImageView)(getActivity().findViewById(R.id.header))).setImageDrawable(drawable);
        }

        return rootView;
    }
}


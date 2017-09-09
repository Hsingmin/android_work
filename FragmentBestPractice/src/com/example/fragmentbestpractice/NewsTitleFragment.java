package com.example.fragmentbestpractice;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class NewsTitleFragment extends Fragment implements OnItemClickListener {

	private ListView newsTitleListView;
	
	private List<News> newsList;
	
	private NewsAdapter adapter;
	
	private boolean isTwoPane;
	
	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		newsList = getNews();
		adapter = new NewsAdapter(activity, R.layout.news_item, newsList);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.news_title_frag, container, false);
		newsTitleListView = (ListView) view.findViewById(R.id.news_title_list_view);
		newsTitleListView.setAdapter(adapter);
		newsTitleListView.setOnItemClickListener(this);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		if(getActivity().findViewById(R.id.news_content_layout) != null){
			isTwoPane = true;
		} else{
			isTwoPane = false;
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id){
		News news = newsList.get(position);
		if(isTwoPane){
			NewsContentFragment newsContentFragment = (NewsContentFragment)
					getFragmentManager().findFragmentById(R.id.news_content_fragment);
			newsContentFragment.refresh(news.getTitle(), news.getContent());		
			} else{
				NewsContentActivity.actionStart(getActivity(), news.getTitle(),
						news.getContent());
			}
	}
	
	private List<News> getNews(){
		List<News> newsList = new ArrayList<News>();
		News news1 = new News();
		news1.setTitle("The former Spain striker says the Chelsea forward is keen to return");
		news1.setContent("Diego Costa is the ideal striker for Atletico Madrid, "
				+ "former Rojiblancos striker Kiko says, but he believes the club's transfer ban could scupper a deal for the Chelsea forward."
				+ "Agent: I've discussed Courtois with Real Costa has had two spells at Atletico already and was particularly successful"
				+ " between 2012 and 2014 under Diego Simeone, helping the Madrid team to La Liga and also the Champions League final before a move to Stamford Bridge."
				+ "The Brazil-born forward, who now represents Spain at international level, has won two Premier League titles in his three seasons at Chelsea, "
				+ "but recently revealed he had been told by Blues boss Antonio Conte that he is not in the Italian's plans for the 2017-18 season."
				+ "Atletico are interested in bringing back their former forward, but failure to overturn their transfer ban for irregularities"
				+ " in the signing of young players means he would be unable to turn out for the Rojiblancos until January.");
		newsList.add(news1);
		
		News news2 = new News();
		news2.setTitle("The Blues still appear to be well placed to secure the Brazil wing-back for what will likely be a record fee for a defender");
		news2.setContent("Reports in Italy have suggested that the Brazilian wing-back is also wanted by Manchester City and Paris Saint-Germain, "
				+ "but Chelsea are still clear favourites to complete a deal."
				+ "Sandro is happy to make the move to west London and stands to pocket a huge pay rise that will more than double his current salary, "
				+ "which is less than £50,000-a-week. "
				+ "Juventus aren't keen to sell but they are financially limited in comparison to the spending power of the Premier League's top clubs"
				+ " and are aiming to squeeze the best possible fee out of Chelsea. ");
		newsList.add(news2);
		
		return newsList;
	}
}














 
package thelookcompany.lookcares.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.util.ArrayList;

public class FitFragment extends Fragment {

	public ArrayList<BackStack> hMapTabs = new ArrayList<BackStack>();
	public BackFragmentCallback mCallback ;
	
	public int stackSize() {
		return hMapTabs.size();
	}
	
	public void backFragment() {
		BackStack backStack = (BackStack)hMapTabs.get(hMapTabs.size() - 1);
		BackStack newStack = hMapTabs.get(hMapTabs.size() - 2);
		hMapTabs.remove(hMapTabs.size() - 1);
		FragmentManager manager = getChildFragmentManager();
		FragmentTransaction ft = manager.beginTransaction();
		ft.replace(newStack.containerID, newStack.fragment);
		ft.commit();
        if (backStack != null && backStack.fragment != null) {
            //backStack.fragment.onDestroy();
        }
		
		if (mCallback != null) 
			mCallback.backFragment(newStack);
	}
	
	public void addFragment(Fragment f, int containerID, int type) {
		BackStack newStack = new BackStack();
		newStack.fragment = f;
		newStack.containerID = containerID;
		newStack.type = type;
		
		hMapTabs.add(newStack);
		
		FragmentManager manager = getChildFragmentManager();
		FragmentTransaction ft = manager.beginTransaction();
		
		ft.replace(containerID, f);
		ft.commit();
	}
	
	public interface BackFragmentCallback {
		public abstract void backFragment(BackStack f);
	}
	
}

package com.maustudios.sfs2x;
import com.smartfoxserver.v2.core.SFSEventType;
import com.smartfoxserver.v2.extensions.SFSExtension;

public class MyExtension extends SFSExtension {

	@Override
	public void init() {
		// TODO Auto-generated method stub
		addEventHandler(SFSEventType.USER_LOGIN, LoginHandler.class);
		addRequestHandler("friend_request", FriendRequest.class);
	}
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		super.destroy();
	}

}

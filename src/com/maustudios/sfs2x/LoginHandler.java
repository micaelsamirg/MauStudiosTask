package com.maustudios.sfs2x;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.core.SFSEventParam;
import com.smartfoxserver.v2.entities.data.SFSArray;
import com.smartfoxserver.v2.exceptions.SFSErrorCode;
import com.smartfoxserver.v2.exceptions.SFSErrorData;
import com.smartfoxserver.v2.exceptions.SFSException;
import com.smartfoxserver.v2.exceptions.SFSLoginException;
import com.smartfoxserver.v2.extensions.BaseServerEventHandler;
import com.smartfoxserver.v2.extensions.ExtensionLogLevel;

public class LoginHandler extends BaseServerEventHandler {

	@Override
	public void handleServerEvent(ISFSEvent event) throws SFSException {
		String username = (String) event.getParameter(SFSEventParam.LOGIN_NAME);

		try {
			Connection conn = getParentExtension().getParentZone().getDBManager().getConnection();

			PreparedStatement sql = conn.prepareStatement("SELECT id FROM players WHERE name = ?");
			sql.setString(1, username);

			ResultSet result = sql.executeQuery();

			if (result == null || (result != null && result.getFetchSize() == 0)) {
				SFSErrorData errData = new SFSErrorData(SFSErrorCode.LOGIN_BAD_USERNAME);
				errData.addParameter(username);

				throw new SFSLoginException("user not found!", errData);
			}

			SFSArray row = SFSArray.newFromResultSet(result);

			conn.close();

			trace("Login successful, joining room!");

		} catch (SQLException e) {
			trace(ExtensionLogLevel.WARN, " SQL Failed: " + e.toString());
		}
	}
}
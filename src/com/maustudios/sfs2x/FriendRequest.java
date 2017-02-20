package com.maustudios.sfs2x;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSArray;
import com.smartfoxserver.v2.exceptions.SFSRuntimeException;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

public class FriendRequest extends BaseClientRequestHandler {

	@Override
	public void handleClientRequest(User player, ISFSObject params) {
		// TODO Auto-generated method stub
		try {

			String relationShip = params.getUtfString("relationShip");
			String playerName = params.getUtfString("player_name");
			
			if (relationShip == null || (relationShip != null && relationShip.length() == 0)) {

				throw new SFSRuntimeException("relationship is mandatory");
			}

			if (playerName == null || (playerName != null && playerName.length() == 0)) {

				throw new SFSRuntimeException("player name is mandatory");
			}
			
			/*
			 * CREATE TABLE `friend_request` (
				  `id` int(11) NOT NULL AUTO_INCREMENT,
				  `relationship` varchar(100) NOT NULL,
				  `player_id` int(11) NOT NULL,
				  PRIMARY KEY (`id`),
				  KEY `friend_request_players_fk` (`player_id`),
				  CONSTRAINT `friend_request_players_fk` FOREIGN KEY (`player_id`) REFERENCES `players` (`id`)
				) ENGINE=InnoDB DEFAULT CHARSET=utf8
			 */
			
			Connection conn = getParentExtension().getParentZone().getDBManager().getConnection();
			
			PreparedStatement sql = conn.prepareStatement("SELECT id FROM players WHERE name = ?");
			sql.setString(1, playerName);
			
			ResultSet result = sql.executeQuery();

			if (result == null || (result != null && result.getFetchSize() == 0)) {
				throw new SFSRuntimeException("player name not found");
			}

			SFSArray row = SFSArray.newFromResultSet(result);
			
			String insertTableSQL = "INSERT INTO friend_request"
					+ "(relationship, player_id) VALUES"
					+ "(?,?)";
			PreparedStatement preparedStatement =  conn.prepareStatement(insertTableSQL);
			preparedStatement.setString(1, relationShip);
			preparedStatement.setInt(2, row.getInt(0));
			
			preparedStatement .executeUpdate();
			
			trace("Friend request sent successfully!");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}

import java.io.IOException;

import com.daloji.blockchain.core.Block;
import com.daloji.blockchain.core.commons.database.proxy.LevelDbProxy;


/**
 * Verification des blocks headers deja telechargé
 * @author daloji
 *
 */
public class LevelDbCheck {

	public static void main(String[] args) throws IOException {
		int nb = 0;
		Block bloc = new Block();
		String genesisHash = bloc.getHashGenesisBloc();
		String previoushash = genesisHash;
		String lasthash = LevelDbProxy.getInstance().getLastHash();
		bloc = LevelDbProxy.getInstance().findBlock(lasthash);
		while(bloc!=null) {
			nb++;
			previoushash = bloc.getPrevBlockHash();
			bloc = LevelDbProxy.getInstance().findBlock(previoushash);
			if(bloc != null) {
				System.out.println(bloc);
			}
		}
		System.out.println("nb block telecharge " +nb);
	}

}

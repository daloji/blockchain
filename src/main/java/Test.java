import java.io.IOException;

import org.iq80.leveldb.DBIterator;

import com.daloji.blockchain.core.Crypto;
import com.daloji.blockchain.core.commons.database.proxy.LevelDbProxy;
import com.daloji.blockchain.core.utils.Utils;

public class Test {

	public static void main(String[] args) throws IOException {
		
		DBIterator dbiterator = LevelDbProxy.getInstance().getIterator();
		try {
			for(dbiterator.seekToFirst(); dbiterator.hasNext(); dbiterator.next()) {
				String key = Utils.bytesToHex(dbiterator.peekNext().getKey());
				byte[] block = dbiterator.peekNext().getValue();
			//	System.out.println(" keys :"+Utils.StrLittleEndian(key));
			}
		}catch (Exception e) {
			// TODO: handle exception
		} finally {
			// Make sure you close the iterator to avoid resource leaks.
			dbiterator.close();
		}
		
		System.out.println(Utils.StrLittleEndian("12C94A527AD80F95136357F3DD26AFE1EE71E5EE59F646DFC2C6FAD8D8DEC810"));

		String str ="0200000001881AD35B628BE9CEB3D2A44F90E122C10B0C2FD11B3AB51CAA4A0F77F6E6D1DA010000006B4830450221008DD4B719B7316F5D26EE0CC22B3FF05B6F49699EA2E3BCB059C91463C7353B16022042E3A73CE1374D772F03A1F9E3EA5C4CDBC20C9D9AAFA6438EE6ACCC443C10B4012102DE587BA3B6F64E8BD70D8078DBC33FDD80B44024A4D4A1FE6BFF840B4B669B8EFDFFFFFF0236282000000000001976A914101CE1EB6EEE436E5753286ECEA48D5B7097BE7D88AC36CE9909000000001976A914D90B315D8C162E638A9CF477A602A394123E7F1F88ACF26F0900";
		
	
		byte[] hashbyte = Crypto.doubleSha256(Utils.hexStringToByteArray(str));
		
		System.out.println(Utils.bytesToHex(hashbyte));
		//LevelDbProxy.getInstance().checkBlocChainStatus();
		/*Date date = new Date(1231469665);
		System.out.println(date);
		System.out.println(Utils.StrLittleEndian("6FE28C0AB6F1B372C1A6A246AE63F74F931E8365E15A089C68D6190000000000"));
		System.out.println(Utils.StrLittleEndian("4860EB18BF1B1620E37E9490FC8A427514416FD75159AB86688E9A8300000000"));
		System.out.println(Utils.StrLittleEndian("214F1824D6B2EB5F201C6780488187FE72C608BD66F078B51C2BAECE00000000"));
		System.out.println(Utils.StrLittleEndian("4944469562AE1C2C74D9A535E00B6F3E40FFBAD4F2FDA3895501B58200000000"));
		System.out.println(Utils.StrLittleEndian("2F8B9D4D8EA162A1D2E5FE288B110BF80A92B963B2D30F40956C88A200000000"));

		String hash = LevelDbProxy.getInstance().getObject("LAST_HASH");	
		System.out.println(Utils.StrLittleEndian(hash));


		String ip ="2A020168AC05000300000242AC110002";
		System.out.println(Utils.StrLittleEndian(ip));
		//LevelDbProxy.getInstance().checkBlocChainStatus();
		 */
		
		
		/*Block bloc = new Block();
		bloc.setPrevBlockHash("previous");
		bloc.setTime(122223300);
		bloc.setTxCount(1);
		LevelDbProxy.getInstance().addBlock(bloc);
		bloc = new Block();
		bloc.setPrevBlockHash("previous1");
		bloc.setTime(1222435);
		bloc.setTxCount(1);
		LevelDbProxy.getInstance().addBlock(bloc);
		Block blockreceive = LevelDbProxy.getInstance().findBlock(bloc.generateHash());
		LevelDbProxy.getInstance().deleteBlock(bloc.generateHash());
		blockreceive = LevelDbProxy.getInstance().findBlock(bloc.generateHash());
		String hash = LevelDbProxy.getInstance().getObject("LAST_HASH");
		LevelDbProxy.getInstance().closeDatabase();*/
		
	
		
		
		String value = Utils.StrLittleEndian("010000");
		int size =Integer.parseInt(value,16);

		System.out.println(size);
	}
}

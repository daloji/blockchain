import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.iq80.leveldb.DBIterator;

import com.daloji.blockchain.core.commons.proxy.LevelDbProxy;
import com.daloji.blockchain.core.utils.Utils;
import com.google.common.collect.Lists;

public class Test {

	public static void main(String[] args) throws IOException {
	
		DBIterator dbiterator = LevelDbProxy.getInstance().getIterator();
		try {
		  for(dbiterator.seekToFirst(); dbiterator.hasNext(); dbiterator.next()) {
		    String key = Utils.bytesToHex(dbiterator.peekNext().getKey());
		    byte[] block = dbiterator.peekNext().getValue();
		    System.out.println(" keys :"+Utils.StrLittleEndian(key));
		  }
		}catch (Exception e) {
			// TODO: handle exception
		} finally {
		  // Make sure you close the iterator to avoid resource leaks.
			dbiterator.close();
		}
		/*Date date = new Date(1231469665);
		System.out.println(date);
		System.out.println(Utils.StrLittleEndian("6FE28C0AB6F1B372C1A6A246AE63F74F931E8365E15A089C68D6190000000000"));
		System.out.println(Utils.StrLittleEndian("4860EB18BF1B1620E37E9490FC8A427514416FD75159AB86688E9A8300000000"));
		System.out.println(Utils.StrLittleEndian("214F1824D6B2EB5F201C6780488187FE72C608BD66F078B51C2BAECE00000000"));
		System.out.println(Utils.StrLittleEndian("4944469562AE1C2C74D9A535E00B6F3E40FFBAD4F2FDA3895501B58200000000"));
		System.out.println(Utils.StrLittleEndian("5E2B8043BD9F8DB558C284E00EA24F78879736F4ACD110258E48C22700000000"));
*/
		
	
}
}

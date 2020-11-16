/* AppInit.java

{{IS_NOTE
	Proposito: 
        * Ler as configuracoes escritas no ficheiro zksandbox.properties. Apartir dessas configuracoes,
        * criar objectos Item e objectos Category.
        * Criar um mapeamento dos objectos Category/Item
		
	Descricao:
        * Eata classe implementa a interface WebAppInit. Esta classe é chamada logo no inicio antes da 
        * construcao da pagina inicial.
        * Para que ela seja chamada, anota-se nas propriedades de leitura do ficheiro WEB-INF/zk.xml
        * Esta classe contem um metodo estatico que retorna o mapa criado. Por tanto qualquer classe que pretende
        * aceder as Categorias e os seus Item, deve chamar o metodo "getCateMap()".
		
	Historico:
		Nov 12, 2008 2:49:39 PM , Created by jumperchen
                * Editado: 29/06/2013
}}IS_NOTE
*/
package esira.webapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.servlet.ServletContext;
import org.zkoss.util.logging.Log;
import org.zkoss.zk.ui.WebApp;
import org.zkoss.zk.ui.util.WebAppInit;

/**
 * @author jumperchen
 *
 */
public class AppInit implements WebAppInit{

	private static final Log log = Log.lookup(AppInit.class);
	
	final static String PATH = "/";
	final static String CONFIG = "zksandbox.properties";
	final static String CATEGORY_TYPE = "CATEGORY";
	final static String LINK_TYPE = "LINK";
	
        // Criacao do objecto _cateMap que guarda o Mapa de Categorias/Item
	private static Map _cateMap = new LinkedHashMap () {
		 public Object remove(Object key) {
			 throw new UnsupportedOperationException();
		 }
		 public void clear() {
			 throw new UnsupportedOperationException();			 
		 }
	};
	
        //No inicio da classe e feito a leitura das configuracoes escritas no ficheiro zksandbox.properties
	public void init(WebApp wapp) throws Exception {
		loadProperites(wapp.getServletContext());
	}
	
        //Retorna o mapeamento das Categorias com os seus Item
	static Map getCateMap() {
		return _cateMap;
	}
        
        /* Le as configuracao do ficheiro zksandbox.properties, cria objectos Category/Item e coloca-os no objecto
           mapa "_cateMap" */
	private void loadProperites(ServletContext context) {
		try {
			BufferedReader bufReader = new BufferedReader(
					new InputStreamReader(context.getResourceAsStream(PATH + CONFIG)));
			String prop = null;
			while ((prop = bufReader.readLine()) != null) {
				int begin = prop.indexOf("=");
				if (prop.startsWith("#") || prop.startsWith("!") || begin == -1)
					continue;
				
				final String key = prop.substring(0, begin).trim();
				final String values = prop.substring(begin + 1, prop.length()).trim();
				final String[] vals = values.split(",");
				if (vals.length == 0)
					continue;
				final String arg0 = vals[0].trim();
				if (CATEGORY_TYPE.equals(arg0)) {
					if (vals.length < 3) {
						log.error("This category has no enough argument: size less than 3, for example, CATEGORY,IconURL,Label");
						continue;
					}
					Category cate = new Category(key, vals[1].trim(), vals[2].trim(), null);
					_cateMap.put(key, cate);
				} else if (LINK_TYPE.equals(arg0) ) {
					if (vals.length < 4) {
						log.error("This category has no enough argument: size less than 4, for example, LINK,IconURL,Label,Href");
						continue;
					}
					Category cate = new Category(key, vals[1].trim(), vals[2].trim(), vals[3].trim());
					_cateMap.put(key, cate);
				} else {
					Category cate = (Category) _cateMap.get(arg0);
					if (cate == null) {
						log.error("This category is undefined: " + arg0);
						continue;
					}
					if (vals.length < 4) {
						log.error("This demo item has no enough argument: size less than 4, for example, categoryId,FileURL,IconURL,Label");
						continue;
					}
					// [ItemId=CategoryId, Demo File URL, Icon URL, Label]
					Item item = new Item(key, arg0, vals[1].trim(), vals[2].trim(), vals[3].trim());
					cate.addItem(item);
				}
			}
			bufReader.close();
		} catch (IOException e) {
			log.error("Ingored: failed to load a properties file, \nCause: "
					+ e.getMessage());
		}
	}
	
}

/* Category.java

 {{IS_NOTE
 Proposito: 
 * Criar um objecto Category
		
 Descricao:
 * Esta classe contem os atributos e metodos de uma categoria. Dentre os atributos de uma categoria, 
 * existe uma lista de todos Item pertencentes a ele, o que diferencia de uma classe "org.zkoss.zul.Button"
        
 Historico:
 Nov 12, 2008 10:39:23 AM , Created by jumperchen
 * Editado: 29/06/2013	
 }}IS_NOTE
 */
package esira.webapp;

import java.util.LinkedList;
import java.util.List;

/**
 * @author jumperchen
 */
public class Category {

    private String _id;
    private String _icon;
    private String _label;
    private String _href;
    private List _items;

    public Category(String id, String icon, String label, String href) {
        _id = id;
        _icon = icon;
        _label = label;
        _items = new LinkedList() {
            public Object remove(int index) {
                throw new UnsupportedOperationException();
            }
        };
        _href = href;
    }

    public void addItem(Item item) {
        _items.add(item);
    }

    public String getHref() {
        return _href;
    }

    public List getItems() {
        return _items;
    }

    public String getId() {
        return _id;
    }

    public String getIcon() {
        return _icon;
    }

    public String getLabel() {
        return _label;
    }
}

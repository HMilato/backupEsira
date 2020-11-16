/* Item.java

 {{IS_NOTE
 Proposito: 
 * Criar um objecto Item
		
 Descricao:
 * Esta classe contem os atributos e metodos de um Item. Dentre os atributos de um Item, para alem
 * do seu proprio id tambem contem o id da categoria na qual ele pertence, o que 
 * diferencia da classe "org.zkoss.zul.Listitem".
 * Esta classe implementa a interface "java.io.Serializable" para o uso do metodo "toString()". O metodo 
 * "toString()" é usado nesta classe para mostrar respectivamente o seu id e o 
 * file (URL da pagina associada ao Item).
		
 Historico:
 Nov 12, 2008 10:39:23 AM , Created by jumperchen
 * Editado: 29/06/2013		
 }}IS_NOTE
 */
package esira.webapp;

/**
 * @author jumperchen
 */
public class Item implements java.io.Serializable {

    private String _id;
    private String _cateId;
    private String _icon;
    private String _file;
    private String _label;

    public Item(String id, String cateId, String file, String icon, String label) {
        _id = id;
        _cateId = cateId;
        _icon = icon;
        _file = file;
        _label = label;
    }

    public String getId() {
        return _id;
    }

    public String getCateId() {
        return _cateId;
    }

    public String getIcon() {
        return _icon;
    }

    public String getFile() {
        return _file;
    }

    public String getLabel() {
        return _label;
    }

    @Override
    public String toString() {
        return "[DemoItem:" + _id + ", " + _file + ']';
    }
}

package esira;

import esira.dao.ConnectionFactory;
import esira.domain.Curso;
import esira.domain.Faculdade;
import esira.domain.Listaadmissao;
import esira.domain.Users;
import esira.service.CRUDService;
import esira.service.CRUDServiceImpl;
import esira.service.UserAutentic;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//import static javafx.scene.input.KeyCode.T;
import org.springframework.security.core.context.SecurityContextHolder;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Messagebox;
import java.sql.ResultSet;

public class MyViewModel {
        @WireVariable
        private CRUDService csimp;
        
	private int count;

	@Init
	public void init() throws SQLException {
              
        }
        

	@Command
	@NotifyChange("count")
	public void cmd() {
		++count;
	}
        
        @Command
        public void verDados(){
             
        }

	public int getCount() {
		return count;
	}
        
        @Command
        public void dadoslogin(){
              csimp = (CRUDService) SpringUtil.getBean("CRUDService");
              Users u = csimp.GetUniqueEntityByNamedQuery("Users.findByUtilizador", "admin");
             Messagebox.show("Godd boy"+u);
        }
}

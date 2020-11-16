/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package esira;

import entidade.Docentews;
import entidade.EstudanteGraduado;
import entidade.EstudantesMatriculado;
import entidade.Funcionariows;
import entidade.Planoscurriculares;
import esira.domain.Disciplina;
import esira.domain.Estudante;
import esira.domain.Faculdade;
import esira.domain.Inscricao;
import esira.domain.Users;
import esira.hibernate.MultiTenantConnectionProviderImpl;
import esira.hibernate.TenantIdResolver;
import esira.service.CRUDService;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.HashMap;
import java.util.Map;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.zkoss.zul.Messagebox;

/**
 *
 * @author user
 */
@Controller
public class RestFulModuleController {

    @Autowired
    @Qualifier("CRUDService")
    CRUDService crudService;

    @RequestMapping(value = "/ul/all",
            method = RequestMethod.GET,
            produces = {MimeTypeUtils.APPLICATION_JSON_VALUE},
            headers = {"Accept=application/json"})
    public ResponseEntity<Object> studentsAll() {
        try {
            List<Estudante> list = crudService.findByJPQueryFilter("from Estudante", null, 0, 3);
            
            if(list == null){
                return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.OK);
            }
             ResponseEntity<Object> re=new ResponseEntity<Object>(list, HttpStatus.OK);
//             re.getHeaders().add("Authorization", "Basic YWxhZGRpbjpvcGVuc2VzYW1l");
            return re;
            //new ResponseEntity<Object>(list, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/user/all",
            method = RequestMethod.GET,
            produces = {MimeTypeUtils.APPLICATION_JSON_VALUE},
            headers = "Accept=application/json")
    public ResponseEntity<Object> users() {
        try {
            List<Users> list = crudService.getAll(Users.class);

            if(list == null){
                return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.OK);
            }
            return new ResponseEntity<Object>(list, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/enroll/{id}",
            method = RequestMethod.GET,
            produces = {MimeTypeUtils.APPLICATION_JSON_VALUE},
            headers = "Accept=application/json")
    public ResponseEntity<Object> enroll(@PathVariable("id") String id) {
        try {
            Map<String, Object> par = new HashMap<String, Object>();
            par.put("id", id);
            List<Inscricao> list = crudService.findByJPQuery("SELECT i FROM Inscricao i WHERE i.idEstudante.idEstudante = :id", par);

            if(list == null){
                return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.OK);
            }
            
            return new ResponseEntity<Object>(list, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
    @RequestMapping(value = "/subjest/all",
            method = RequestMethod.GET,
            produces = {MimeTypeUtils.APPLICATION_JSON_VALUE},
            headers = "Accept=application/json")
    public ResponseEntity<Object> subjetsAll() {
        try {
            List<Disciplina> list = crudService.getAll(Disciplina.class);
            
            if(list == null){
                return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.OK);
            }
            
            return new ResponseEntity<Object>(list, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    
    @RequestMapping(value = "/faculty/all",
            method = RequestMethod.GET,
            produces = {MimeTypeUtils.APPLICATION_JSON_VALUE},
            headers = "Accept=application/json")
    public ResponseEntity<Object> facultyall() {
        try {
            List<Faculdade> list = crudService.getAll(Faculdade.class);
            
            if(list == null){
                return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.OK);
            }
            
            return new ResponseEntity<Object>(list, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    

    @RequestMapping(value = "/ul/estudantesmatriculados/{anoletivo}",
            method = RequestMethod.GET,
            produces = {MimeTypeUtils.APPLICATION_JSON_VALUE},
            headers = "Accept=application/json")
    public ResponseEntity<Object> estudantesMatriculados(@PathVariable("anoletivo") int anoletivo) {
        try {
        
        String sql = "(select e.id_estudante, nome_completo, nr_estudante, e.nuit as nuitE, to_char(data_nascimento, 'DD-MM-YYYY') as dataNascimento, ano_ingresso, e.turno as regime, pais.descricao as pais, tipodocumento.descricao as tipoDocumentos, nr_documento, provincia.descricao as provinciaadmi, masculino, provincia.descricao as provincianas, distritocampus, nivel_frequencia, curso.abreviatura as cursoCod, is_bolseiro as bolseiro, delegacao.cod_delegacao as coddelegacao, delegacao.delegacao as delegacaoNome, delegacao.endereco as delegacaoEndereco, matricula.anulada as anulada, bolsa.descricao as nomeBolsa, instituicaobolsa.nuit as nuitInstituicaoBolsa\n"+
                   "from  pais, tipodocumento, documento, provincia, curso, delegacao,  matricula, bolsa, estudante as e LEFT JOIN ingressobolseiro ON e.id_estudante = ingressobolseiro.id_estudante LEFT JOIN  instituicaobolsa on instituicaobolsa.nome_instituicao = ingressobolseiro.doador\n" +
                   "where nacionalidade = id_pais and e.id_estudante = documento.id_estudante and documento.tipo = tipodocumento.id_tipo and escolaprovincia = id_provincia and curso.id_curso = e.cursocurrente and e.ano_ingresso <= ? and matricula.id_estudante = e.id_estudante and matricula.ano = ? and delegacao.iddelegacao = e.iddelegacao and bolsa.id_bolsa = e.bolsa \n"+  
                   ")";
   
        PreparedStatement pstm = new MultiTenantConnectionProviderImpl().getConnection(TenantIdResolver.getTenant()).prepareStatement(sql);        
        pstm.setInt(1, anoletivo);
        pstm.setInt(2, anoletivo);
        
        List<EstudantesMatriculado> list = new ArrayList<>();
        ResultSet rs = pstm.executeQuery();
        
	while (rs.next()) { 

            EstudantesMatriculado em = new EstudantesMatriculado();
            em.setNomecompleto(rs.getString("nome_completo"));
            em.setBolsanome(rs.getString("nomeBolsa"));
            em.setNrEstudante(rs.getString("nr_estudante"));
            em.setNuit(rs.getInt("nuitE"));
            em.setDatanasc(rs.getString("dataNascimento"));
            em.setAnoingresso(rs.getInt("ano_ingresso"));
            
            String regime = rs.getString("regime");
            if(regime.equalsIgnoreCase("1")){
              em.setRegime("LAB");  
            }else if(regime.equalsIgnoreCase("2")){
              em.setRegime("PLA");
            }else{
               em.setRegime("EAD"); 
            }
         
            em.setPaisnasc(rs.getString("pais"));
            em.setDocumentotipo(rs.getString("tipoDocumentos"));
            em.setDocumentonumero(rs.getString("nr_documento"));
            em.setProvinciacandidatura(rs.getString("provinciaadmi"));
            String sexo = rs.getString("masculino");
            if(sexo.equalsIgnoreCase("t")){
                em.setSexo("01");
            }
            else{
                em.setSexo("02");
            } 

            em.setProvincianasc(rs.getString("provincianas"));
            em.setDistritocampus(rs.getInt("distritocampus"));
            em.setNivel(rs.getInt("nivel_frequencia"));
            em.setCurso(rs.getString("cursoCod"));
            em.setBolseiro(rs.getBoolean("bolseiro"));
            em.setInstituicaonuit(rs.getInt("nuitInstituicaoBolsa"));
            em.setDelegacao(rs.getString("coddelegacao"));
            em.setDelegacaonome(rs.getString("delegacaoNome"));
            em.setEndereco(rs.getString("delegacaoEndereco"));
            em.setMatriculaanulada(rs.getBoolean("anulada"));
            list.add(em); 
        }
//        rs.close();
//        pstm.close();   
//        
//        Map<String, List> mapa = new HashMap<String, List>();
//        mapa.put("Estudantes", list);
// 
//            if(mapa == null){
//               return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.OK);
//            }           
//              return new ResponseEntity<Object>(mapa, HttpStatus.OK);
//        } catch (Exception e) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
        
        
        
               rs.close();
        pstm.close();    
  
        Map<String, List> mapa = new HashMap<String, List>();
        mapa.put("Estudantes", list);
     
            if(mapa == null){
               return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.OK);
            }           
              return new ResponseEntity<Object>(mapa, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } 
        
        
        
        
    }    
    
        @RequestMapping(value = "/ul/estudantesgraduados/{anoletivo}",
            method = RequestMethod.GET,
            produces = {MimeTypeUtils.APPLICATION_JSON_VALUE},
            headers = "Accept=application/json")
    public ResponseEntity<Object> estudantesGraduados(@PathVariable("anoletivo") Integer anoletivo) {
        try {

       String sql = "(select e.id_estudante, nome_completo, nr_estudante, e.nuit as nuitE, to_char(data_nascimento, 'DD-MM-YYYY') as dataNascimento, ano_ingresso, e.turno as regime, pais.descricao as pais, tipodocumento.descricao as tipoDocumentos, nr_documento, provincia.descricao as provinciaadmi, masculino, provincia.descricao as provincianas, distritocampus, nivel_frequencia, curso.abreviatura as cursoCod, is_bolseiro as bolseiro, delegacao.cod_delegacao as coddelegacao, delegacao.delegacao as delegacaoNome, delegacao.endereco as delegacaoEndereco, matricula.anulada as anulada, bolsa.descricao as nomeBolsa, instituicaobolsa.nuit as nuitInstituicaoBolsa, e.anograduacao as anograduacao\n"+
                   "from  pais, tipodocumento, documento, provincia, curso, delegacao,  matricula, bolsa, estudante as e LEFT JOIN ingressobolseiro ON e.id_estudante = ingressobolseiro.id_estudante LEFT JOIN  instituicaobolsa on instituicaobolsa.nome_instituicao = ingressobolseiro.doador\n" +
                   "where nacionalidade = id_pais and e.id_estudante = documento.id_estudante and documento.tipo = tipodocumento.id_tipo and escolaprovincia = id_provincia and curso.id_curso = e.cursocurrente and e.ano_ingresso <= ? and matricula.id_estudante = e.id_estudante and matricula.ano = ? and delegacao.iddelegacao = e.iddelegacao and bolsa.id_bolsa = e.bolsa \n"+  
                   ")";
            
            PreparedStatement pstm = new MultiTenantConnectionProviderImpl().getConnection(TenantIdResolver.getTenant()).prepareStatement(sql);        
            pstm.setInt(1, anoletivo);
            pstm.setInt(2, anoletivo);
        
            List list = new ArrayList<>();
            ResultSet rs = pstm.executeQuery();

            while (rs.next()) { 
                
            EstudanteGraduado em = new EstudanteGraduado();
            em.setNomecompleto(rs.getString("nome_completo"));
            em.setBolsanome(rs.getString("nomeBolsa"));
            em.setNrEstudante(rs.getString("nr_estudante"));
            em.setNuit(rs.getInt("nuitE"));
            em.setDatanasc(rs.getString("dataNascimento"));
            em.setAnoingresso(rs.getInt("ano_ingresso"));    
            
            String regime = rs.getString("regime");
            if(regime.equalsIgnoreCase("1")){
              em.setRegime("LAB");  
            }else if(regime.equalsIgnoreCase("2")){
              em.setRegime("PLA");
            }else{
               em.setRegime("EAD"); 
            }

            em.setPaisnasc(rs.getString("pais"));
            em.setDocumentotipo(rs.getString("tipoDocumentos"));
            em.setDocumentonumero(rs.getString("nr_documento"));
            em.setProvinciacandidatura(rs.getString("provinciaadmi"));
            String sexo = rs.getString("masculino");
            if(sexo.equalsIgnoreCase("t")){
                em.setSexo("01");
            }
            else{
                em.setSexo("02");
            } 
            em.setProvincianasc(rs.getString("provincianas"));
            em.setDistritocampus(rs.getInt("distritocampus"));
            em.setNivel(rs.getInt("nivel_frequencia"));
            em.setCurso(rs.getString("cursoCod"));
            em.setBolseiro(rs.getBoolean("bolseiro"));
            em.setInstituicaonuit(rs.getInt("nuitInstituicaoBolsa"));
            em.setDelegacao(rs.getString("coddelegacao"));
            em.setDelegacaonome(rs.getString("delegacaoNome"));
            em.setEndereco(rs.getString("delegacaoEndereco"));
      
            Integer anograduacao = rs.getInt("anograduacao");
            if(anograduacao.equals(anoletivo)){
               em.setGraduado(true);               
            }
            list.add(em);   
        }
        rs.close();
        pstm.close();    
  
        Map<String, List> mapa = new HashMap<String, List>();
        mapa.put("Estudantes", list);
     
            if(mapa == null){
               return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.OK);
            }           
              return new ResponseEntity<Object>(mapa, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
   }     
    
  
       @RequestMapping(value = "/ul/listadocentes/{anoletivo}",
            method = RequestMethod.GET,
            produces = {MimeTypeUtils.APPLICATION_JSON_VALUE},
            headers = "Accept=application/json")
    public ResponseEntity<Object> listaDocentes(@PathVariable("anoletivo") int anoletivo) {
        try {
   
           
        String sql = "(Select nuit, nome, masculino, docidentnum, contrato, grau, habilitacaoescola, areaformacao, paisorigem, to_char(datanasc, 'DD-MM-YYYY') as datanasc, isdocente, anocontratacao\n" +
                     "FROM funcionario where anocontratacao <= ?)";
            
        PreparedStatement pstm = new MultiTenantConnectionProviderImpl().getConnection(TenantIdResolver.getTenant()).prepareStatement(sql);        
        pstm.setInt(1, anoletivo);
        
        List<Docentews> list = new ArrayList<>();
  
        ResultSet rs = pstm.executeQuery();
        
	while (rs.next()) { 

            Docentews fs = new Docentews();
            
            fs.setNomeCompleto(rs.getString("nome"));
            fs.setNuit(rs.getInt("nuit"));
            boolean sexo = rs.getBoolean("masculino");
               if(sexo){
                fs.setSexo("01");
            }else{
                fs.setSexo("02");
            } 
            fs.setDocidentnum(rs.getString("docidentnum"));
            fs.setContrato(rs.getString("contrato"));
            fs.setHabilitacao(rs.getString("grau"));
            fs.setHabilitacaoescola(rs.getString("habilitacaoescola"));
            fs.setAreaformacao(rs.getString("areaformacao"));
            fs.setPaisorigem(rs.getString("paisorigem"));
            fs.setDatanasc(rs.getString("datanasc"));
            fs.setIsdocente(rs.getBoolean("isdocente"));
     
            list.add(fs);
        }
        rs.close();
        pstm.close();   
        
        Map<String, List> mapa = new HashMap<String, List>();
        mapa.put("Docentes", list);
 
            if(mapa == null){
               return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.OK);
            }           
              return new ResponseEntity<Object>(mapa, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
   }   
    
       @RequestMapping(value = "/ul/listafuncionarios",
            method = RequestMethod.GET,
            produces = {MimeTypeUtils.APPLICATION_JSON_VALUE},
            headers = "Accept=application/json")
    public ResponseEntity<Object> obterFuncionarios() {
        try {
           
        String sql = "(Select nuit, nome, masculino, docidentnum, contrato, grau, habilitacaoescola, areaformacao, paisorigem, to_char(datanasc, 'DD-MM-YYYY') as datanasc \n" +
                     "FROM funcionario)";
            
        PreparedStatement pstm = new MultiTenantConnectionProviderImpl().getConnection(TenantIdResolver.getTenant()).prepareStatement(sql);        
        List list = new ArrayList<>();
  
        ResultSet rs = pstm.executeQuery();
        
	while (rs.next()) { 

            Funcionariows fs = new Funcionariows();
            
            fs.setNomeCompleto(rs.getString("nome"));
            fs.setNuit(rs.getInt("nuit"));
            boolean sexo = rs.getBoolean("masculino");
               if(sexo){
                fs.setSexo("01");
            }else{
                fs.setSexo("02");
            } 
            fs.setDocidentnum(rs.getString("docidentnum"));
            fs.setContrato(rs.getString("contrato"));
            fs.setHabilitacao(rs.getString("grau"));
            fs.setHabilitacaoescola(rs.getString("habilitacaoescola"));
            fs.setAreaformacao(rs.getString("areaformacao"));
            fs.setPaisorigem(rs.getString("paisorigem"));
            fs.setDatanasc(rs.getString("datanasc"));
            
            list.add(fs);
        }
        rs.close();
        pstm.close();  
        
        Map<String, List> mapa = new HashMap<String, List>();
        mapa.put("Funcionarios", list);
 
            if(mapa == null){
               return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.OK);
            }           
              return new ResponseEntity<Object>(mapa, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
   } 
    
    
    @RequestMapping(value = "/ul/planoscurriculares",
            method = RequestMethod.GET,
            produces = {MimeTypeUtils.APPLICATION_JSON_VALUE},
            headers = "Accept=application/json")
    public ResponseEntity<Object> planosCurriculares() {
        try {
        
        String sql = "(SELECT planocurricular.codplanoc as codplano, planocurricular.nome as planoC, planocurricular.ativo as ativo, curso.codigo_curso as cc, curso.descricao as curso, curso.cursograu as cg, curso.cursoareaformacao as carea, disciplina.codigo as codigo, disciplina.nome as disciplina, areacientifica.descricao as areacientifica, disciplina.nivel as nivel, disciplina.semestre as semestre, disciplina.credito as credito\n" +
                        "from planocurricular, curso, disciplina, areacientifica\n" +
                         "where planocurricular.idcurso = curso.id_curso and disciplina.curso = curso.id_curso and areacientifica.idarea = disciplina.area_cientifica )";
            
        PreparedStatement pstm = new MultiTenantConnectionProviderImpl().getConnection(TenantIdResolver.getTenant()).prepareStatement(sql);        
        List<Planoscurriculares> list = new ArrayList<>();
  
        ResultSet rs = pstm.executeQuery();
        
	while (rs.next()) { 

           Planoscurriculares pc = new Planoscurriculares();
           
           pc.setPlanonome(rs.getString("planoC"));
           pc.setPlano(rs.getString("codplano"));
           pc.setAtivo(rs.getBoolean("ativo"));
           pc.setCurso(rs.getString("cc"));
           pc.setCursonome(rs.getString("curso"));
           pc.setCursograu(rs.getString("cg"));
           pc.setCursoareaformacao(rs.getString("carea"));
           pc.setDisciplina(rs.getString("codigo")); 
           pc.setDisciplinanome(rs.getString("disciplina"));         
           pc.setAreacientificanome(rs.getString("areacientifica"));
           pc.setNivel(rs.getInt("nivel"));
           pc.setSemestre(rs.getInt("semestre"));
           pc.setCredito(rs.getInt("credito"));
           
           list.add(pc);
                   
        }
        rs.close();
        pstm.close();  
        
        Map<String, List> mapa = new HashMap<String, List>();
        mapa.put("Planos", list);
 
            if(mapa == null){
               return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.OK);
            }           
              return new ResponseEntity<Object>(mapa, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
   }     
    
        
    
}

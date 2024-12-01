package org.fpij.jitakyoei.model.unit;

import java.util.List;
import org.fpij.jitakyoei.business.EntidadeBOImpl;
import org.fpij.jitakyoei.model.beans.Entidade;
import org.fpij.jitakyoei.model.dao.DAO;
import org.fpij.jitakyoei.model.dao.DAOImpl;
import org.fpij.jitakyoei.view.AppView;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class EntidadeBOImplUnitTest {

    private DAO<Entidade> dao;
    @Mock
    private AppView appView;

    @Before
    public void setup() {
        dao = new DAOImpl<Entidade>(Entidade.class); 
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateEntidadeSucesso() throws Exception {
        Entidade entidade = new Entidade();
        entidade.setNome("Test Entity");
        entidade.setTelefone1("(11) 1234-5678");
 
        EntidadeBOImpl entidadeBO = new EntidadeBOImpl(appView);
        entidadeBO.createEntidade(entidade);

        List<Entidade> entidades = dao.list();
        assertTrue(entidades.contains(entidade)); 
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateEntidadeFalha() throws Exception {
        Entidade entidade = new Entidade();  
        
        EntidadeBOImpl entidadeBO = new EntidadeBOImpl(appView);
        entidadeBO.createEntidade(entidade);
    }

    @Test
    public void testListAllEntidadeSucesso() throws Exception {
        EntidadeBOImpl entidadeBO = new EntidadeBOImpl(appView);
        Entidade entidade1 = new Entidade();
        entidade1.setNome("Entidade 1");
        entidadeBO.createEntidade(entidade1);

        Entidade entidade2 = new Entidade();
        entidade2.setNome("Entidade 2");
        entidadeBO.createEntidade(entidade2);

        List<Entidade> result = entidadeBO.listAll();

        assertNotNull(result);
        assertTrue(result.size() > 0); 
    }

    @Test
    public void testSearchEntidadeSucesso() throws Exception {
        Entidade entidade = new Entidade();
        entidade.setNome("Entidade Pesquisa");
        
        EntidadeBOImpl entidadeBO = new EntidadeBOImpl(appView);
        entidadeBO.createEntidade(entidade);
        
        List<Entidade> result = entidadeBO.searchEntidade(entidade);

        assertNotNull(result);
        assertTrue(result.size() > 0); 
    }

    @Test(expected = AssertionError.class)
    public void testSearchEntidadeFalha() throws Exception {
        Entidade entidade = new Entidade();
        entidade.setNome("Entidade Inexistente");
        
        EntidadeBOImpl entidadeBO = new EntidadeBOImpl(appView);
        entidadeBO.searchEntidade(entidade);  
    }

    @Test
    public void testUpdateEntidadeSucesso() throws Exception {
        Entidade entidade = new Entidade();
        entidade.setNome("Entidade a ser Atualizada");

        EntidadeBOImpl entidadeBO = new EntidadeBOImpl(appView);
        entidadeBO.createEntidade(entidade);

        entidade.setNome("Entidade Atualizada");

        entidadeBO.updateEntidade(entidade);

        List<Entidade> entidades = entidadeBO.searchEntidade(entidade);
        assertTrue(entidades.stream().anyMatch(e -> e.getNome().equals("Entidade Atualizada")));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateEntidadeFalha() throws Exception {
        Entidade entidade = new Entidade();
        entidade.setNome("Entidade Inexistente");
        
        EntidadeBOImpl entidadeBO = new EntidadeBOImpl(appView);
        entidadeBO.updateEntidade(entidade);
    }
}

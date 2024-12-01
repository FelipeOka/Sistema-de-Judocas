package org.fpij.jitakyoei.model.unit;

import java.util.Arrays;
import java.util.List;
import org.fpij.jitakyoei.business.EntidadeBOImpl;
import org.fpij.jitakyoei.business.ProfessorBOImpl;
import org.fpij.jitakyoei.business.ProfessorEntidadeBOImpl;
import org.fpij.jitakyoei.model.beans.Entidade;
import org.fpij.jitakyoei.model.beans.Professor;
import org.fpij.jitakyoei.model.beans.ProfessorEntidade;
import org.fpij.jitakyoei.model.dao.DAO;
import org.fpij.jitakyoei.model.dao.DAOImpl;
import org.fpij.jitakyoei.view.AppView;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ProfessorEntidadeBOImplUnitTest {

    private DAO<ProfessorEntidade> dao;
    @Mock
    private AppView appView;

    @Before
    public void setup() {
        dao = new DAOImpl<>(ProfessorEntidade.class);
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateProfessorEntidadeSucesso() throws Exception {
        Professor professor = new Professor();
        professor.getFiliado().setNome("Professor Teste");
        professor.getFiliado().setId(1L);
        ProfessorBOImpl professorBO = new ProfessorBOImpl(appView);
        professorBO.createProfessor(professor);

        Entidade entidade = new Entidade();
        entidade.setNome("Test Entity");
        entidade.setTelefone1("(11) 1234-5678");

        EntidadeBOImpl entidadeBO = new EntidadeBOImpl(appView);
        entidadeBO.createEntidade(entidade);

        ProfessorEntidade professorEntidade1 = new ProfessorEntidade(professor, entidade);

        List<ProfessorEntidade> relacionamentos = Arrays.asList(professorEntidade1);

        ProfessorEntidadeBOImpl professorEntidadeBO = new ProfessorEntidadeBOImpl(appView);
        professorEntidadeBO.createProfessorEntidade(relacionamentos);

        assertNotEquals(0, dao.list().size());
    }

    @Test(expected = Exception.class)
    public void testCreateProfessorEntidadeIllegalArgumentException() throws Exception {
        ProfessorEntidade professorEntidade = new ProfessorEntidade(null, null);
        List<ProfessorEntidade> relacionamentos = Arrays.asList(professorEntidade);

        ProfessorEntidadeBOImpl professorEntidadeBO = new ProfessorEntidadeBOImpl(appView);
        professorEntidadeBO.createProfessorEntidade(relacionamentos);
    }

}

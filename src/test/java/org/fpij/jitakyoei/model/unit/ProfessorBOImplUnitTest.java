package org.fpij.jitakyoei.model.unit;

import java.util.List;
import org.fpij.jitakyoei.business.ProfessorBOImpl;
import org.fpij.jitakyoei.model.beans.Professor;
import org.fpij.jitakyoei.model.dao.DAO;
import org.fpij.jitakyoei.model.dao.DAOImpl;
import org.fpij.jitakyoei.view.AppView;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ProfessorBOImplUnitTest {

    private DAO<Professor> dao;
    @Mock
    private AppView appView;

    private ProfessorBOImpl professorBO;

    @Before
    public void setup() {
        dao = new DAOImpl<>(Professor.class);

        MockitoAnnotations.openMocks(this);
        professorBO = new ProfessorBOImpl(appView);
    }

    @Test
    public void testCreateProfessorSucesso() throws Exception {
        Professor professor = new Professor();
        professor.getFiliado().setNome("Professor Teste");
        professor.getFiliado().setId(1L);

        professorBO.createProfessor(professor);

        List<Professor> professors = dao.list();
        assertNotNull(professors);
        assertTrue(professors.contains(professor));
    }

    @Test
    public void testUpdateProfessorSucesso() throws Exception {
        Professor professor = new Professor();
        professor.getFiliado().setNome("Professor Teste");
        professor.getFiliado().setId(2L);

        dao.save(professor);

        professor.getFiliado().setNome("Professor Atualizado");
        professorBO.updateProfessor(professor);

        List<Professor> professors = dao.list();
        assertNotNull(professors);
        assertTrue(professors.contains(professor));
        assertEquals("Professor Atualizado", professor.getFiliado().getNome());
    }

    @Test
    public void testListAllProfessor() throws Exception {
        Professor professor1 = new Professor();
        professor1.getFiliado().setNome("Professor 1");
        professor1.getFiliado().setId(1L);
        dao.save(professor1);

        Professor professor2 = new Professor();
        professor2.getFiliado().setNome("Professor 2");
        professor2.getFiliado().setId(2L);
        dao.save(professor2);

        List<Professor> result = professorBO.listAll();

        assertNotNull(result);
        assertTrue(result.contains(professor1));
        assertTrue(result.contains(professor2));
    }

    @Test
    public void testSearchProfessor() throws Exception {
        Professor professor = new Professor();
        professor.getFiliado().setNome("Professor Teste");
        professor.getFiliado().setId(3L);
        dao.save(professor);

        List<Professor> result = professorBO.searchProfessor(professor);

        assertNotNull(result);
        assertTrue(result.contains(professor));
    }

    @Test(expected = Exception.class)
    public void testCreateProfessor_IllegalArgumentException() throws Exception {

        Professor professor = new Professor();
        professor.setFiliado(null);
        professorBO.createProfessor(professor);
    }

    @Test(expected = Exception.class)
    public void testUpdateProfessor_IllegalArgumentException() throws Exception {

        Professor professor = new Professor();
        professor.setFiliado(null);
        professorBO.updateProfessor(professor);
    }

}

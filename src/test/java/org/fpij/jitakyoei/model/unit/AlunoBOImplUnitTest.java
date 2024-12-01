package org.fpij.jitakyoei.model.unit;

import java.util.List;
import org.fpij.jitakyoei.business.AlunoBOImpl;
import org.fpij.jitakyoei.model.beans.Aluno;
import org.fpij.jitakyoei.model.beans.Filiado;
import org.fpij.jitakyoei.view.AppView;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class AlunoBOImplUnitTest {

    @Mock
    private AppView appView;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateSucesso() throws Exception {
        Aluno aluno = new Aluno();
        Filiado filiado = new Filiado();
        aluno.setFiliado(filiado);

        AlunoBOImpl alunoBO = new AlunoBOImpl(appView);
        alunoBO.createAluno(aluno);

        assertNotNull(aluno.getFiliado().getId());
    }

    @Test(expected = Exception.class)
    public void testCreateFalhaSemFiliado() throws Exception {
        Aluno aluno = new Aluno();

        AlunoBOImpl alunoBO = new AlunoBOImpl(appView);
        alunoBO.createAluno(aluno);
    }
    
    @Test(expected = Exception.class)
    public void testUpdateAlunoFalhaDados() throws Exception {
        Aluno aluno = new Aluno();
        Filiado filiado = new Filiado();
        aluno.setFiliado(filiado);

        Aluno alunoExistente = new Aluno();
        alunoExistente.setFiliado(new Filiado());

        AlunoBOImpl alunoBO = new AlunoBOImpl(appView);
        alunoBO.updateAluno(aluno);
        
        Mockito.verify(appView).handleModelChange(alunoExistente);
    }


    @Test(expected = Exception.class)
    public void testUpdateAlunoErroGenerico() throws Exception {
        Aluno aluno = new Aluno();

        AlunoBOImpl alunoBO = new AlunoBOImpl(appView);
        alunoBO.updateAluno(aluno);
    }

    @Test
    public void testSearchAlunoSucesso() throws Exception {
        Aluno aluno = new Aluno();
        List<Aluno> alunosMock = List.of(new Aluno(), new Aluno());

        AlunoBOImpl alunoBO = new AlunoBOImpl(appView);
        List<Aluno> result = alunoBO.searchAluno(aluno);

        assertNotNull(result);
        assertNotEquals(0,result.size());
    }

    @Test
    public void testListAllSucesso() throws Exception {
        List<Aluno> alunosMock = List.of(new Aluno(), new Aluno(), new Aluno());

        AlunoBOImpl alunoBO = new AlunoBOImpl(appView);
        List<Aluno> result = alunoBO.listAll();

        assertNotNull(result);
        assertNotEquals(0, result.size());
    }

}

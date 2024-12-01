package org.fpij.jitakyoei.model.dao;

import java.util.Date;
import java.util.List;
import org.fpij.jitakyoei.model.beans.Aluno;
import org.fpij.jitakyoei.model.beans.Endereco;
import org.fpij.jitakyoei.model.beans.Entidade;
import org.fpij.jitakyoei.model.beans.Filiado;
import org.fpij.jitakyoei.model.beans.Professor;
import org.fpij.jitakyoei.util.DatabaseManager;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.BeforeClass;
import org.junit.Test;

public class AlunoTest {

    private static DAO<Aluno> alunoDao;
    private static Aluno aluno;
    private static Entidade entidade;
    private static Endereco endereco;
    private static Filiado f1;
    private static Filiado filiadoProf;
    private static Professor professor;

    @BeforeClass
    public static void setUp() {
        DatabaseManager.setEnviroment(DatabaseManager.TEST);
        f1 = new Filiado();
        f1.setNome("Aécio");
        f1.setCpf("036.464.453-27");
        f1.setDataNascimento(new Date());
        f1.setDataCadastro(new Date());
        f1.setId(1332L);

        endereco = new Endereco();
        endereco.setBairro("Dirceu");
        endereco.setCep("64078-213");
        endereco.setCidade("Teresina");
        endereco.setEstado("PI");
        endereco.setRua("Rua Des. Berilo Mota");

        filiadoProf = new Filiado();
        filiadoProf.setNome("Professor");
        filiadoProf.setCpf("036.464.453-27");
        filiadoProf.setDataNascimento(new Date());
        filiadoProf.setDataCadastro(new Date());
        filiadoProf.setId(3332L);
        filiadoProf.setEndereco(endereco);

        professor = new Professor();
        professor.setFiliado(filiadoProf);

        entidade = new Entidade();
        entidade.setEndereco(endereco);
        entidade.setNome("Academia 1");
        entidade.setTelefone1("(086)1234-5432");

        aluno = new Aluno();
        aluno.setFiliado(f1);
        aluno.setProfessor(professor);
        aluno.setEntidade(entidade);

        alunoDao = new DAOImpl<Aluno>(Aluno.class);
    }

    public static void clearDatabase() {
        List<Aluno> all = alunoDao.list();
        for (Aluno each : all) {
            alunoDao.delete(each);
        }
        assertEquals(0, alunoDao.list().size());
    }

    @Test
    public void testSalvarAlunoComAssociassoes() throws Exception {
        clearDatabase();

        alunoDao.save(aluno);
        assertEquals("036.464.453-27", alunoDao.get(aluno).getFiliado().getCpf());
        assertEquals("Aécio", alunoDao.get(aluno).getFiliado().getNome());
        assertEquals("Professor", alunoDao.get(aluno).getProfessor().getFiliado().getNome());
        assertEquals("Dirceu", alunoDao.get(aluno).getProfessor().getFiliado().getEndereco().getBairro());
    }

    @Test(expected = Exception.class)
    public void testSalvarAlunoComDadosInvalidos() {
        clearDatabase();

        Aluno alunoInvalido = new Aluno();
        alunoInvalido.setFiliado(null);

        alunoDao.save(alunoInvalido);
    }

    @Test(expected = Exception.class)
    public void testSalvarAlunoComDadosObrigatoriosAusentes() throws Exception {
        clearDatabase();

        Aluno alunoInvalido = new Aluno();
        alunoInvalido.setFiliado(new Filiado());

        alunoDao.save(alunoInvalido);
    }

    @Test
    public void testAtualizarAluno() throws Exception {
        clearDatabase();
        assertEquals(0, alunoDao.list().size());

        alunoDao.save(aluno);
        assertEquals(1, alunoDao.list().size());
        assertEquals("Aécio", aluno.getFiliado().getNome());

        Aluno a1 = alunoDao.get(aluno);
        a1.getFiliado().setNome("TesteUpdate");
        alunoDao.save(a1);

        Aluno a2 = alunoDao.get(a1);
        assertEquals("TesteUpdate", a2.getFiliado().getNome());
        assertEquals(1, alunoDao.list().size());
    }

    @Test(expected = Exception.class)
    public void testAtualizarAlunoInexistente() throws Exception {
        clearDatabase();

        Aluno alunoNaoExistente = new Aluno();
        alunoNaoExistente.setFiliado(new Filiado());
        alunoNaoExistente.getFiliado().setNome("Nao Existente");
    }

    @Test(expected = Exception.class)
    public void testAtualizarAlunoComDadosInvalidos() throws Exception {
        clearDatabase();

        alunoDao.save(aluno);

        Aluno alunoInvalido = alunoDao.get(aluno);
        alunoInvalido.getFiliado().setCpf("123");

        alunoDao.save(alunoInvalido);
    }

    @Test(expected = Exception.class)
    public void testAtualizarAlunoComDadosObrigatoriosAusentes() throws Exception {
        clearDatabase();

        alunoDao.save(aluno); 

        Aluno alunoInvalido = alunoDao.get(aluno);
        alunoInvalido.getFiliado().setNome(null);

        alunoDao.save(alunoInvalido); 
    }

    @Test
    public void testSearchAluno() throws Exception {
        clearDatabase();
        alunoDao.save(aluno);

        Filiado f = new Filiado();
        f.setNome("Aécio");
        Aluno a = new Aluno();
        a.setFiliado(f);

        List<Aluno> result = alunoDao.search(a);
        assertEquals(1, result.size());
        assertEquals("036.464.453-27", result.get(0).getFiliado().getCpf());

        clearDatabase();
        assertEquals(0, alunoDao.search(a).size());
    }

    @Test
    public void testSearchAlunoInexistente() {
        clearDatabase();

        Filiado f = new Filiado();
        f.setNome("Inexistente");
        Aluno a = new Aluno();
        a.setFiliado(f);

        List<Aluno> result = alunoDao.search(a);
        assertEquals(0, result.size());
    }

    @Test
    public void testDeletarAluno() throws Exception {
        clearDatabase();

        alunoDao.save(aluno);
        assertEquals(1, alunoDao.list().size());

        alunoDao.delete(aluno);
        assertEquals(0, alunoDao.list().size());
    }

    @Test(expected = Exception.class)
    public void testDeletarAlunoInexistente() throws Exception {
        clearDatabase();

        alunoDao.delete(aluno);
    }

    @AfterClass
    public static void closeDatabase() {
        clearDatabase();
        DatabaseManager.close();
    }

}

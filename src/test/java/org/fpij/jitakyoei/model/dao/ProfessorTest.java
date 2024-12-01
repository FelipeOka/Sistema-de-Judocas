package org.fpij.jitakyoei.model.dao;

import java.util.Date;
import java.util.List;
import org.fpij.jitakyoei.model.beans.Endereco;
import org.fpij.jitakyoei.model.beans.Filiado;
import org.fpij.jitakyoei.model.beans.Professor;
import org.fpij.jitakyoei.util.DatabaseManager;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.BeforeClass;
import org.junit.Test;

public class ProfessorTest {

    private static DAO<Professor> professorDao;
    private static Professor professor;
    private static Filiado filiadoProf;
    private static Endereco endereco;

    @BeforeClass
    public static void setUp() {
        DatabaseManager.setEnviroment(DatabaseManager.TEST);

        endereco = new Endereco();
        endereco.setBairro("Dirceu");
        endereco.setCep("64078-213");
        endereco.setCidade("Teresina");
        endereco.setEstado("PI");
        endereco.setRua("Rua Des. Berilo Mota");

        filiadoProf = new Filiado();
        filiadoProf.setNome("Professor Teste");
        filiadoProf.setCpf("123.456.789-00");
        filiadoProf.setDataNascimento(new Date());
        filiadoProf.setDataCadastro(new Date());
        filiadoProf.setEndereco(endereco);

        professor = new Professor();
        professor.setFiliado(filiadoProf);

        professorDao = new DAOImpl<>(Professor.class);
    }

    public static void clearDatabase() {
        List<Professor> all = professorDao.list();
        for (Professor each : all) {
            professorDao.delete(each);
        }
        assertEquals(0, professorDao.list().size());
    }

    @Test
    public void testSalvarProfessorComDadosValidos() throws Exception {
        clearDatabase();

        professorDao.save(professor);
        Professor savedProfessor = professorDao.get(professor);

        assertNotNull(savedProfessor);
        assertEquals("Professor Teste", savedProfessor.getFiliado().getNome());
        assertEquals("123.456.789-00", savedProfessor.getFiliado().getCpf());
        assertEquals("Dirceu", savedProfessor.getFiliado().getEndereco().getBairro());
    }

    @Test(expected = Exception.class)
    public void testSalvarProfessorSemFiliado() throws Exception {
        clearDatabase();

        Professor professorInvalido = new Professor();
        professorInvalido.setFiliado(null);

        professorDao.save(professorInvalido);
    }

    @Test
    public void testSalvarBuscarProfessorInexistente() throws Exception {
        clearDatabase();

        Filiado filiadoBusca = new Filiado();
        filiadoBusca.setNome("Professor Não Existente");
        Professor professorBusca = new Professor();
        professorBusca.setFiliado(filiadoBusca);

        List<Professor> resultado = professorDao.search(professorBusca);
        assertEquals(0, resultado.size());
    }

    @Test(expected = Exception.class)
    public void testSalvarProfessorComDadosInvalidos() throws Exception {
        clearDatabase();

        Filiado filiadoInvalido = new Filiado();
        filiadoInvalido.setNome("Professor Invalido");
        filiadoInvalido.setCpf("123");
        filiadoInvalido.setDataNascimento(new Date());
        filiadoInvalido.setEndereco(endereco);

        Professor professorInvalido = new Professor();
        professorInvalido.setFiliado(filiadoInvalido);

        professorDao.save(professorInvalido);
    }

    @Test(expected = Exception.class)
    public void testSalvarProfessorComDadosObrigatoriosAusentes() throws Exception {
        clearDatabase();

        Filiado filiadoInvalido = new Filiado();
        filiadoInvalido.setNome("Professor Sem CPF");
        filiadoInvalido.setCpf("");
        filiadoInvalido.setDataNascimento(new Date());
        filiadoInvalido.setEndereco(endereco);

        Professor professorInvalido = new Professor();
        professorInvalido.setFiliado(filiadoInvalido);

        professorDao.save(professorInvalido);
    }

    @Test
    public void testAtualizarProfessor() throws Exception {
        clearDatabase();

        professorDao.save(professor);
        Professor savedProfessor = professorDao.get(professor);

        assertNotNull(savedProfessor);
        assertEquals("Professor Teste", savedProfessor.getFiliado().getNome());

        savedProfessor.getFiliado().setNome("Professor Atualizado");
        professorDao.save(savedProfessor);

        Professor updatedProfessor = professorDao.get(savedProfessor);
        assertEquals("Professor Atualizado", updatedProfessor.getFiliado().getNome());
    }

    @Test(expected = Exception.class)
    public void testAtualizarProfessorInexistente() throws Exception {
        clearDatabase();

        Professor professorInexistente = new Professor();
        professorInexistente.setFiliado(filiadoProf);

        professorDao.save(professorInexistente);
    }

    @Test(expected = Exception.class)
    public void testAtualizarProfessorComDadosInvalidos() throws Exception {
        clearDatabase();

        professorDao.save(professor);

        Professor professorInvalido = professorDao.get(professor);
        professorInvalido.getFiliado().setCpf("123");

        professorDao.save(professorInvalido);
    }

    @Test(expected = Exception.class)
    public void testAtualizarProfessorComDadosObrigatoriosAusentes() throws Exception {
        clearDatabase();

        professorDao.save(professor);

        Professor professorInvalido = professorDao.get(professor);
        professorInvalido.getFiliado().setNome(null);

        professorDao.save(professorInvalido);
    }

    @Test
    public void testBuscarProfessorPorNome() throws Exception {
        clearDatabase();

        professorDao.save(professor);

        Filiado filiadoBusca = new Filiado();
        filiadoBusca.setNome("Professor Teste");
        Professor professorBusca = new Professor();
        professorBusca.setFiliado(filiadoBusca);

        List<Professor> resultado = professorDao.search(professorBusca);
        assertEquals(1, resultado.size());
        assertEquals("123.456.789-00", resultado.get(0).getFiliado().getCpf());
    }

    @Test
    public void testBuscarProfessorInexistente() throws Exception {
        clearDatabase();

        Filiado filiadoBusca = new Filiado();
        filiadoBusca.setNome("Professor Não Existente");
        Professor professorBusca = new Professor();
        professorBusca.setFiliado(filiadoBusca);

        List<Professor> resultado = professorDao.search(professorBusca);
        assertEquals(0, resultado.size());
    }

    @Test
    public void testDeletarProfessor() throws Exception {
        clearDatabase();

        professorDao.save(professor);
        assertEquals(1, professorDao.list().size());

        professorDao.delete(professor);
        assertEquals(0, professorDao.list().size());
    }

    @Test(expected = Exception.class)
    public void testDeletarProfessorInexistente() throws Exception {
        clearDatabase();

        professorDao.delete(professor);
    }

    // ------------------------- TESTES DE CAIXA BRANCA -------------------------
    @Test(expected = Exception.class)
    public void testSalvarProfessorComCPFInvalido() throws Exception {
        clearDatabase();

        Filiado filiadoInvalido = new Filiado();
        filiadoInvalido.setNome("Professor Invalido");
        filiadoInvalido.setCpf("123"); 
        filiadoInvalido.setDataNascimento(new Date());
        filiadoInvalido.setEndereco(endereco);

        Professor professorInvalido = new Professor();
        professorInvalido.setFiliado(filiadoInvalido);

        professorDao.save(professorInvalido);
    }

    @Test(expected = Exception.class)
    public void testSalvarFiliadoComDataNascimentoFutura() throws Exception {
        clearDatabase();

        Filiado filiadoInvalido = new Filiado();
        filiadoInvalido.setNome("Filiado Invalido");
        filiadoInvalido.setCpf("123.456.789-00");
        filiadoInvalido.setDataNascimento(new Date(System.currentTimeMillis() + 1000000000));
        filiadoInvalido.setEndereco(endereco);

        Professor professorInvalido = new Professor();
        professorInvalido.setFiliado(filiadoInvalido);

        professorDao.save(professorInvalido);
    }

    @Test(expected = Exception.class)
    public void testSalvarProfessorComCPFNulo() throws Exception {
        clearDatabase();

        Filiado filiadoInvalido = new Filiado();
        filiadoInvalido.setNome("Professor Sem CPF");
        filiadoInvalido.setCpf("");
        filiadoInvalido.setDataNascimento(new Date());
        filiadoInvalido.setEndereco(endereco);

        Professor professorInvalido = new Professor();
        professorInvalido.setFiliado(filiadoInvalido);

        professorDao.save(professorInvalido);
    }

    @AfterClass
    public static void closeDatabase() {
        clearDatabase();
        DatabaseManager.close();
    }
}

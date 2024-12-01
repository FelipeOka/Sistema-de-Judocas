package org.fpij.jitakyoei.model.dao;

import java.util.List;
import org.fpij.jitakyoei.model.beans.Endereco;
import org.fpij.jitakyoei.model.beans.Entidade;
import org.fpij.jitakyoei.util.DatabaseManager;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.BeforeClass;
import org.junit.Test;

public class EntidadeTest {

    private static DAO<Entidade> entidadeDao;
    private static Entidade entidade;
    private static Endereco endereco;

    @BeforeClass
    public static void setUp() {
        DatabaseManager.setEnviroment(DatabaseManager.TEST);

        endereco = new Endereco();
        endereco.setBairro("Centro");
        endereco.setCep("12345-678");
        endereco.setCidade("Cidade Teste");
        endereco.setEstado("TS");
        endereco.setRua("Rua Principal");

        entidade = new Entidade();
        entidade.setNome("Entidade Teste");
        entidade.setTelefone1("(11) 1234-5678");
        entidade.setEndereco(endereco);

        entidadeDao = new DAOImpl<>(Entidade.class);
    }

    public static void clearDatabase() {
        entidadeDao.list().forEach(e -> entidadeDao.delete(e));
        assertEquals(0, entidadeDao.list().size());
    }

    @Test
    public void testSalvarEntidadeComDadosValidos() throws Exception {
        clearDatabase();

        entidadeDao.save(entidade);
        Entidade savedEntidade = entidadeDao.get(entidade);

        assertNotNull(savedEntidade);
        assertEquals("Entidade Teste", savedEntidade.getNome());
        assertEquals("Centro", savedEntidade.getEndereco().getBairro());
        assertEquals("(11) 1234-5678", savedEntidade.getTelefone1());
    }

    @Test(expected = Exception.class)
    public void testSalvarEntidadeSemNome() throws Exception {
        clearDatabase();

        Entidade entidadeSemNome = new Entidade();
        entidadeSemNome.setTelefone1("(11) 9876-5432");
        entidadeSemNome.setEndereco(endereco);

        entidadeDao.save(entidadeSemNome);
    }

    @Test(expected = Exception.class)
    public void testSalvarEntidadeComTelefoneInvalido() throws Exception {
        clearDatabase();

        Entidade entidadeInvalida = new Entidade();
        entidadeInvalida.setNome("Academia Teste");
        entidadeInvalida.setTelefone1("12345"); 
        entidadeInvalida.setEndereco(endereco);

        entidadeDao.save(entidadeInvalida); 
    }

    @Test
    public void testAtualizarEntidade() throws Exception {
        clearDatabase();

        entidadeDao.save(entidade);
        Entidade savedEntidade = entidadeDao.get(entidade);

        assertNotNull(savedEntidade);
        assertEquals("Entidade Teste", savedEntidade.getNome());

        savedEntidade.setNome("Entidade Atualizada");
        entidadeDao.save(savedEntidade);

        Entidade updatedEntidade = entidadeDao.get(savedEntidade);
        assertEquals("Entidade Atualizada", updatedEntidade.getNome());
    }

    @Test(expected = Exception.class)
    public void testAtualizarEntidadeInexistente() throws Exception {
        clearDatabase();

        Entidade entidadeInexistente = new Entidade();
        entidadeInexistente.setNome("Inexistente");
        entidadeInexistente.setTelefone1("(11) 9999-8888");
        entidadeInexistente.setEndereco(endereco);

        entidadeDao.save(entidadeInexistente);
    }

    @Test
    public void testAtualizarProcurarEntidadePorNome() throws Exception {
        clearDatabase();

        entidadeDao.save(entidade); 

        Entidade entidadeBusca = new Entidade();
        entidadeBusca.setNome("Entidade Teste"); 

        List<Entidade> resultado = entidadeDao.search(entidadeBusca);
        assertEquals(1, resultado.size()); 
        assertEquals("Centro", resultado.get(0).getEndereco().getBairro());
    }

    @Test(expected = Exception.class)
    public void testAtualizarEntidadeComTelefoneInvalido() throws Exception {
        clearDatabase();

        entidadeDao.save(entidade);

        Entidade entidadeInvalida = entidadeDao.get(entidade);
        entidadeInvalida.setTelefone1("12345");

        entidadeDao.save(entidadeInvalida);
    }

    @Test(expected = Exception.class)
    public void testAtualizarEntidadeComNomeAusente() throws Exception {
        clearDatabase();

        entidadeDao.save(entidade);

        Entidade entidadeInvalida = entidadeDao.get(entidade);
        entidadeInvalida.setNome(null);

        entidadeDao.save(entidadeInvalida);
    }

    @Test
    public void testBuscarEntidadePorNome() throws Exception {
        clearDatabase();

        entidadeDao.save(entidade);

        Entidade entidadeBusca = new Entidade();
        entidadeBusca.setNome("Entidade Teste");

        List<Entidade> resultado = entidadeDao.search(entidadeBusca);
        assertEquals(1, resultado.size());
        assertEquals("Centro", resultado.get(0).getEndereco().getBairro());
    }

    @Test
    public void testBuscarEntidadeInexistente() throws Exception {
        clearDatabase();

        Entidade entidadeBusca = new Entidade();
        entidadeBusca.setNome("Inexistente");

        List<Entidade> resultado = entidadeDao.search(entidadeBusca);
        assertEquals(0, resultado.size());
    }
    
    @Test
    public void testDeletarEntidade() throws Exception {
        clearDatabase();

        entidadeDao.save(entidade);
        assertEquals(1, entidadeDao.list().size());

        entidadeDao.delete(entidade);
        assertEquals(0, entidadeDao.list().size());
    }

    @Test(expected = Exception.class)
    public void testDeletarEntidadeInexistente() throws Exception {
        clearDatabase();

        Entidade entidadeInexistente = new Entidade();
        entidadeInexistente.setNome("Inexistente");
        entidadeInexistente.setEndereco(endereco);

        entidadeDao.delete(entidadeInexistente);
    }

    // ------------------------- TESTES DE CAIXA BRANCA -------------------------
    @Test(expected = Exception.class)
    public void testSalvarEntidadeComNomeVazio() throws Exception {
        clearDatabase();

        Entidade entidadeInvalida = new Entidade();
        entidadeInvalida.setNome("");
        entidadeInvalida.setTelefone1("(11) 98765-4321");
        entidadeInvalida.setEndereco(endereco);

        entidadeDao.save(entidadeInvalida);
    }

    @Test(expected = Exception.class)
    public void testSalvarEntidadeComTelefoneVazio() throws Exception {
        clearDatabase();

        Entidade entidadeInvalida = new Entidade();
        entidadeInvalida.setNome("Academia Teste");
        entidadeInvalida.setTelefone1("");
        entidadeInvalida.setEndereco(endereco);

        entidadeDao.save(entidadeInvalida);
    }

    @AfterClass
    public static void closeDatabase() {
        clearDatabase();
        DatabaseManager.close();
    }
}

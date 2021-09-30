package com.teste;

import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Aplicacao {

	public static void main(String[] args) throws Exception {
		String[] opcaoInicial = { "Leitura", "Criação" };

		int x = JOptionPane.showOptionDialog(null, "Opções para manipulação de xml", "Selecione uma opção",
				JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, opcaoInicial, opcaoInicial[0]);

		switch (x) {
		case 0:
			leituraXML();
			break;
		case 1:
			criacaoXML();
			break;
		}		
	}

	public static void leituraXML() throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = factory.newDocumentBuilder();
		Document documentoXML = documentBuilder.parse("D://produto.xml");

		NodeList listaProdutos = documentoXML.getElementsByTagName("produto");

		int tamanhoListaProdutos = listaProdutos.getLength();
		String produtos = "";

		for (int i = 0; i < tamanhoListaProdutos; i++) {
			Node noProduto = listaProdutos.item(i);

			if (noProduto.getNodeType() == Node.ELEMENT_NODE) {
				Element produto = (Element) noProduto;

				String id = produto.getAttribute("id");

				if (produtos == "") {
					produtos += "Lista de produtos:\n" + id;
				} else {
					produtos += "\n" + id;
				}

				NodeList listaInfoProduto = produto.getChildNodes();

				int tamanhoListaInfo = listaInfoProduto.getLength();

				for (int j = 0; j < tamanhoListaInfo; j++) {
					Node noInfo = listaInfoProduto.item(j);

					if (noInfo.getNodeType() == Node.ELEMENT_NODE) {
						Element info = (Element) noInfo;

						switch (info.getTagName()) {
						case "nome":
							produtos += " " + info.getTextContent();
							break;
						case "quantidade":
							produtos += " - Qtd: " + info.getTextContent();
							break;
						case "valor":
							produtos += " - UN: R$" + info.getTextContent();
							break;
						case "valorTotal":
							produtos += " - Total: R$" + info.getTextContent();
							break;
						}
					}
				}
			}
		}
		if (produtos != "") {
			JOptionPane.showMessageDialog(null, produtos);
		} else {
			JOptionPane.showMessageDialog(null, "Nenhum produto encontrado!");
		}
	}

	public static void criacaoXML()
			throws ParserConfigurationException, TransformerException, SAXException, IOException {
		int idProduto = 0;
		String[] opcaoFinal = { "Sim", "Não" };

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = factory.newDocumentBuilder();

		Document documentoXML = documentBuilder.newDocument();
		Element root = documentoXML.createElement("root");
		documentoXML.appendChild(root);

		int cadastrando = 0;
		while (cadastrando == 0) {
			Element produto = documentoXML.createElement("produto");

			Attr id = documentoXML.createAttribute("id");
			idProduto += 1;
			id.setValue(Integer.toString(idProduto));
			produto.setAttributeNode(id);

			root.appendChild(produto);

			Element nome = documentoXML.createElement("nome");
			String nomeProduto = JOptionPane.showInputDialog("Informe o nome do produto");
			nome.appendChild(documentoXML.createTextNode(nomeProduto));
			produto.appendChild(nome);

			Element quantidade = documentoXML.createElement("quantidade");
			int quantidadeProduto = converteStringParaInt(
					JOptionPane.showInputDialog("Informe a quantidade do produto"));
			quantidade.appendChild(documentoXML.createTextNode(converteIntParaString(quantidadeProduto)));
			produto.appendChild(quantidade);

			Element valor = documentoXML.createElement("valor");
			int valorProduto = converteStringParaInt(JOptionPane.showInputDialog("Informe o valor do produto"));
			valor.appendChild(documentoXML.createTextNode(converteIntParaString(valorProduto)));
			produto.appendChild(valor);

			Element valorTotal = documentoXML.createElement("valorTotal");
			int valorTotalProduto = calculaValorTotal(quantidadeProduto, valorProduto);
			valorTotal.appendChild(documentoXML.createTextNode(converteIntParaString(valorTotalProduto)));
			produto.appendChild(valorTotal);

			cadastrando = JOptionPane.showOptionDialog(null, "Continuar na criação?", "Selecione uma opção",
					JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, opcaoFinal, opcaoFinal[0]);
		}

		TransformerFactory transformerFactory = TransformerFactory.newDefaultInstance();
		Transformer transformer = transformerFactory.newTransformer();

		DOMSource documentoFonte = new DOMSource(documentoXML);

		StreamResult documentoFinal = new StreamResult(new File("D://produto.xml"));

		transformer.transform(documentoFonte, documentoFinal);
		JOptionPane.showMessageDialog(null, "Arquivo gerado com sucesso em: D://produto.xml");

		int i = JOptionPane.showOptionDialog(null, "Deseja fazer a leitura do arquivo gerado?", "Selecione uma opção",
				JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, opcaoFinal, opcaoFinal[0]);
		if (i == 0) {
			leituraXML();
		}
	}

	public static int converteStringParaInt(String valor) {
		int retorno = Integer.parseInt(valor);
		return retorno;
	}

	public static String converteIntParaString(int valor) {
		String retorno = String.valueOf(valor);
		return retorno;
	}

	public static int calculaValorTotal(int quantidade, int valor) {
		int total = 0;
		total = quantidade * valor;
		return total;
	}
}

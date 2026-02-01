package com.nearlone.aicodehelper.ai.rag;

import ch.qos.logback.classic.spi.EventArgUtil;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.splitter.DocumentByParagraphSplitter;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Rag配置
 */
@Configuration
public class RagConfig {

    @Resource
    private EmbeddingModel qwenEmbeddingModel;

    @Resource
    private EmbeddingStore<TextSegment> embeddingStore;

    @Bean
    public ContentRetriever createRagService() {
        // RAG
        // 加载文档
        List<Document> documents = FileSystemDocumentLoader.loadDocuments("src/main/resources/docs");
        // 文档切割：每个文档按照段落进行分割，最大1000个字符，每次最多重叠200个字符
        DocumentByParagraphSplitter documentByParagraphSplitter =
                new DocumentByParagraphSplitter(1000, 200);
        //  自定义文档加载器，把文档转换成向量并保存到向量数据库中
        EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
                .documentSplitter(documentByParagraphSplitter)
                // 为了提高文档质量，为每个切割后的文档碎片 TextSegment 添加文档名成作为元信息
                .textSegmentTransformer(textSegment -> TextSegment.from(textSegment.metadata().getString("file_name") +
                        "\n" + textSegment.text(), textSegment.metadata()))
                // 向量模型
                .embeddingModel(qwenEmbeddingModel)
                // 向量数据库
                .embeddingStore(embeddingStore)
                .build();
        // 加载文档
        ingestor.ingest(documents);
        // 自定义内容加载器
        EmbeddingStoreContentRetriever build = EmbeddingStoreContentRetriever.builder()
                .embeddingStore(embeddingStore)
                .embeddingModel(qwenEmbeddingModel)
                .maxResults(5) //最多五条结果
                .minScore(0.75) //过滤分数小于0.75的向量
                .build();
        return build;

    }
}

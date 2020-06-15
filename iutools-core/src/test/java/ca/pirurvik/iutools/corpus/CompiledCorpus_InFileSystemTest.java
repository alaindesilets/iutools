package ca.pirurvik.iutools.corpus;

import java.io.File;

import com.google.common.io.Files;

import ca.nrc.datastructure.trie.StringSegmenter;

public class CompiledCorpus_InFileSystemTest extends CompiledCorpusTest {

	@Override
	protected CompiledCorpus makeCorpusUnderTest(
			Class<? extends StringSegmenter> segmenterClass) {
		File rootDir = Files.createTempDir();
		CompiledCorpus corpus = new CompiledCorpus_InFileSystem(rootDir);
		corpus.setSegmenterClassName(segmenterClass.getName());
		return corpus;
	}
}

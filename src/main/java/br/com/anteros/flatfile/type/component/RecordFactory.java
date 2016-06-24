package br.com.anteros.flatfile.type.component;



/**
 * @author <a href="http://gilmatryx.googlepages.com/">Gilmar P.S.L.</a>
 *
 * @param <G>
 */
public interface RecordFactory <G extends Record>{

	public abstract G create(String name);
}

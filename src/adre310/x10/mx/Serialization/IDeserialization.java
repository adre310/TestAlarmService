package adre310.x10.mx.Serialization;

import org.apache.http.HttpEntity;

public interface IDeserialization {
	void Deserialization(HttpEntity response) throws Exception;
}

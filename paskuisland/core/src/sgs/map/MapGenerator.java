package sgs.map;

import java.util.Random;

import com.badlogic.gdx.math.Vector2;

import sgs.map.SimplexNoise;

public class MapGenerator {
	
	/**
	 * A 2D Random float Map generator from Sebastian League
	 * Thank you. 
	 * 
	 * @param mapWidth
	 * @param mapHeight
	 * @param seed
	 * @param scale
	 * @param octaves
	 * @param persistance must be between 0 and 1
	 * @param lacunarity must be grater than 1
	 * @param offset
	 * @return
	 */
	public static float[][] GenerateNoiseMap(int mapWidth, int mapHeight, int seed, float scale, int octaves, float persistance, float lacunarity, Vector2 offset) {
		float[][] noiseMap = new float[mapWidth][mapHeight];

		Random prng = new Random(seed);
		Vector2[] octaveOffsets = new Vector2[octaves];
		for (int i = 0; i < octaves; i++) {
			float offsetX = prng.nextFloat()*100000 - 50000 + offset.x;
			float offsetY = prng.nextFloat()*100000 - 50000 + offset.y;
			octaveOffsets [i] = new Vector2 (offsetX, offsetY);
		}

		if (scale <= 0) {
			scale = 0.0001f;
		}

		float maxNoiseHeight = Float.MIN_VALUE;
		float minNoiseHeight = Float.MAX_VALUE;

		float halfWidth = mapWidth / 2f;
		float halfHeight = mapHeight / 2f;


		for (int y = 0; y < mapHeight; y++) {
			for (int x = 0; x < mapWidth; x++) {
		
				float amplitude = 1;
				float frequency = 1;
				float noiseHeight = 0;

				for (int i = 0; i < octaves; i++) {
					float sampleX = (x-halfWidth) / scale * frequency + octaveOffsets[i].x;
					float sampleY = (y-halfHeight) / scale * frequency + octaveOffsets[i].y;

					float perlinValue = (float) (SimplexNoise.noise(sampleX, sampleY) * 2 - 1);
					noiseHeight += perlinValue * amplitude;

					amplitude *= persistance;
					frequency *= lacunarity;
				}

				if (noiseHeight > maxNoiseHeight) {
					maxNoiseHeight = noiseHeight;
				} else if (noiseHeight < minNoiseHeight) {
					minNoiseHeight = noiseHeight;
				}
				noiseMap [x][y] = noiseHeight;
			}
		}

		for (int y = 0; y < mapHeight; y++) {
			for (int x = 0; x < mapWidth; x++) {
				noiseMap [x][y] = (noiseMap [x][y] - minNoiseHeight)/ (maxNoiseHeight - minNoiseHeight);
			}
		}

		return noiseMap;
	}
	
	/**
	 * modifies in place a float map
	 * A 2D Random float Map generator from Sebastian League
	 * Thank you. 
	 * 
	 * @param noiseMap the map to be modified
	 * @param mapWidth
	 * @param mapHeight
	 * @param seed
	 * @param scale
	 * @param octaves
	 * @param persistance must be between 0 and 1
	 * @param lacunarity must be grater than 1
	 * @param offset
	 * @return
	 */
	public static void GenerateNoiseMap(float[][] noiseMap,int mapWidth, int mapHeight, int seed, float scale, int octaves, float persistance, float lacunarity, Vector2 offset) {
		Random prng = new Random(seed);
		Vector2[] octaveOffsets = new Vector2[octaves];
		for (int i = 0; i < octaves; i++) {
			float offsetX = prng.nextFloat()*100000 - 50000 + offset.x;
			float offsetY = prng.nextFloat()*100000 - 50000 + offset.y;
			octaveOffsets [i] = new Vector2 (offsetX, offsetY);
		}

		if (scale <= 0) {
			scale = 0.0001f;
		}

		float maxNoiseHeight = Float.MIN_VALUE;
		float minNoiseHeight = Float.MAX_VALUE;

		float halfWidth = mapWidth / 2f;
		float halfHeight = mapHeight / 2f;


		for (int y = 0; y < mapHeight; y++) {
			for (int x = 0; x < mapWidth; x++) {
		
				float amplitude = 1;
				float frequency = 1;
				float noiseHeight = 0;

				for (int i = 0; i < octaves; i++) {
					float sampleX = (x-halfWidth) / scale * frequency + octaveOffsets[i].x;
					float sampleY = (y-halfHeight) / scale * frequency + octaveOffsets[i].y;

					float perlinValue = (float) (SimplexNoise.noise(sampleX, sampleY) * 2 - 1);
					noiseHeight += perlinValue * amplitude;

					amplitude *= persistance;
					frequency *= lacunarity;
				}

				if (noiseHeight > maxNoiseHeight) {
					maxNoiseHeight = noiseHeight;
				} else if (noiseHeight < minNoiseHeight) {
					minNoiseHeight = noiseHeight;
				}
				noiseMap [x][y] = noiseHeight;
			}
		}

		for (int y = 0; y < mapHeight; y++) {
			for (int x = 0; x < mapWidth; x++) {
				noiseMap [x][y] = (noiseMap [x][y] - minNoiseHeight)/ (maxNoiseHeight - minNoiseHeight);
			}
		}
	}
}


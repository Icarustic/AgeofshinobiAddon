@SideOnly(Side.CLIENT)
public class ModelSwirls extends ModelBase {
	private final ModelRenderer sphere;
	private final ModelRenderer bone12;
	private final ModelRenderer bone11;
	private final ModelRenderer bone7;
	private final ModelRenderer bone;
	private final ModelRenderer bone2;
	private final ModelRenderer bone3;
	private final ModelRenderer bone4;
	private final ModelRenderer bone5;
	private final ModelRenderer bone6;
	private final ModelRenderer bone8;
	private final ModelRenderer bone9;
	private final ModelRenderer bone10;
	private final ModelRenderer bone24;
	private final ModelRenderer bone23;
	private final ModelRenderer bone22;
	private final ModelRenderer bone21;
	private final ModelRenderer bone20;
	private final ModelRenderer bone19;
	private final ModelRenderer bone18;
	private final ModelRenderer bone17;
	private final ModelRenderer bone16;
	private final ModelRenderer bone15;
	private final ModelRenderer bone14;
	private final ModelRenderer bone13;
	private final ModelRenderer bb_main;
	private final ModelRenderer cube_r1;
	private final ModelRenderer cube_r2;
	private final ModelRenderer cube_r3;

	public ModelSwirls() {
		textureWidth = 144;
		textureHeight = 144;

		sphere = new ModelRenderer(this);
		sphere.setRotationPoint(0.0F, 6.0F, 0.0F);
		

		bone12 = new ModelRenderer(this);
		bone12.setRotationPoint(0.0F, -7.0F, 0.0F);
		sphere.addChild(bone12);
		setRotationAngle(bone12, 2.8798F, 0.0F, 0.0F);
		

		bone11 = new ModelRenderer(this);
		bone11.setRotationPoint(0.0F, -7.0F, 0.0F);
		sphere.addChild(bone11);
		setRotationAngle(bone11, 2.618F, 0.0F, 0.0F);
		

		bone7 = new ModelRenderer(this);
		bone7.setRotationPoint(0.0F, -7.0F, 0.0F);
		sphere.addChild(bone7);
		setRotationAngle(bone7, 1.6144F, 0.0F, 0.0F);
		

		bone = new ModelRenderer(this);
		bone.setRotationPoint(0.0F, -6.0F, 0.0F);
		sphere.addChild(bone);
		

		bone2 = new ModelRenderer(this);
		bone2.setRotationPoint(0.0F, -7.0F, 0.0F);
		sphere.addChild(bone2);
		setRotationAngle(bone2, 0.2618F, 0.0F, 0.0F);
		

		bone3 = new ModelRenderer(this);
		bone3.setRotationPoint(0.0F, -7.0F, 0.0F);
		sphere.addChild(bone3);
		setRotationAngle(bone3, 0.5236F, 0.0F, 0.0F);
		

		bone4 = new ModelRenderer(this);
		bone4.setRotationPoint(0.0F, -7.0F, 0.0F);
		sphere.addChild(bone4);
		setRotationAngle(bone4, 0.829F, 0.0F, 0.0F);
		

		bone5 = new ModelRenderer(this);
		bone5.setRotationPoint(0.0F, -7.0F, 0.0F);
		sphere.addChild(bone5);
		setRotationAngle(bone5, 1.0908F, 0.0F, 0.0F);
		

		bone6 = new ModelRenderer(this);
		bone6.setRotationPoint(0.0F, -7.0F, 0.0F);
		sphere.addChild(bone6);
		setRotationAngle(bone6, 1.3526F, 0.0F, 0.0F);
		

		bone8 = new ModelRenderer(this);
		bone8.setRotationPoint(0.0F, -7.0F, 0.0F);
		sphere.addChild(bone8);
		setRotationAngle(bone8, 1.8762F, 0.0F, 0.0F);
		

		bone9 = new ModelRenderer(this);
		bone9.setRotationPoint(0.0F, -7.0F, 0.0F);
		sphere.addChild(bone9);
		setRotationAngle(bone9, 2.0944F, 0.0F, 0.0F);
		

		bone10 = new ModelRenderer(this);
		bone10.setRotationPoint(0.0F, -7.0F, 0.0F);
		sphere.addChild(bone10);
		setRotationAngle(bone10, 2.3562F, 0.0F, 0.0F);
		

		bone24 = new ModelRenderer(this);
		bone24.setRotationPoint(0.0F, 3.18F, 0.0F);
		

		bone23 = new ModelRenderer(this);
		bone23.setRotationPoint(0.0F, -0.29F, 0.0F);
		setRotationAngle(bone23, 0.2618F, 0.0F, 0.0F);
		

		bone22 = new ModelRenderer(this);
		bone22.setRotationPoint(0.0F, -0.29F, 0.0F);
		setRotationAngle(bone22, 0.5236F, 0.0F, 0.0F);
		

		bone21 = new ModelRenderer(this);
		bone21.setRotationPoint(0.0F, -0.29F, 0.0F);
		setRotationAngle(bone21, 0.829F, 0.0F, 0.0F);
		

		bone20 = new ModelRenderer(this);
		bone20.setRotationPoint(0.0F, -0.29F, 0.0F);
		setRotationAngle(bone20, 1.0908F, 0.0F, 0.0F);
		

		bone19 = new ModelRenderer(this);
		bone19.setRotationPoint(0.0F, -0.29F, 0.0F);
		setRotationAngle(bone19, 1.3526F, 0.0F, 0.0F);
		

		bone18 = new ModelRenderer(this);
		bone18.setRotationPoint(0.0F, -0.29F, 0.0F);
		setRotationAngle(bone18, 1.6144F, 0.0F, 0.0F);
		

		bone17 = new ModelRenderer(this);
		bone17.setRotationPoint(0.0F, -0.29F, 0.0F);
		setRotationAngle(bone17, 1.8762F, 0.0F, 0.0F);
		

		bone16 = new ModelRenderer(this);
		bone16.setRotationPoint(0.0F, -0.29F, 0.0F);
		setRotationAngle(bone16, 2.0944F, 0.0F, 0.0F);
		

		bone15 = new ModelRenderer(this);
		bone15.setRotationPoint(0.0F, -0.29F, 0.0F);
		setRotationAngle(bone15, 2.3562F, 0.0F, 0.0F);
		

		bone14 = new ModelRenderer(this);
		bone14.setRotationPoint(0.0F, -0.29F, 0.0F);
		setRotationAngle(bone14, 2.618F, 0.0F, 0.0F);
		

		bone13 = new ModelRenderer(this);
		bone13.setRotationPoint(0.0F, -0.29F, 0.0F);
		setRotationAngle(bone13, 2.8798F, 0.0F, 0.0F);
		

		bb_main = new ModelRenderer(this);
		bb_main.setRotationPoint(0.0F, 24.0F, 0.0F);
		

		cube_r1 = new ModelRenderer(this);
		cube_r1.setRotationPoint(-0.5F, -25.0F, -0.5F);
		bb_main.addChild(cube_r1);
		setRotationAngle(cube_r1, -1.5708F, 0.0F, 2.2253F);
		cube_r1.cubeList.add(new ModelBox(cube_r1, 0, 49, -22.5F, -22.5F, 0.0F, 45, 45, 0, 0.0F, false));

		cube_r2 = new ModelRenderer(this);
		cube_r2.setRotationPoint(-0.5F, -25.0F, -0.5F);
		bb_main.addChild(cube_r2);
		setRotationAngle(cube_r2, -1.5708F, 0.0F, 0.9599F);
		cube_r2.cubeList.add(new ModelBox(cube_r2, 0, 49, -22.5F, -22.5F, 0.0F, 45, 45, 0, 0.0F, false));

		cube_r3 = new ModelRenderer(this);
		cube_r3.setRotationPoint(7.0F, -23.0F, -8.0F);
		bb_main.addChild(cube_r3);
		setRotationAngle(cube_r3, -1.5708F, 0.0F, 0.0F);
		cube_r3.cubeList.add(new ModelBox(cube_r3, 0, 49, -30.0F, -30.0F, -2.0F, 45, 45, 0, 0.0F, false));
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		sphere.render(f5);
		bone24.render(f5);
		bone23.render(f5);
		bone22.render(f5);
		bone21.render(f5);
		bone20.render(f5);
		bone19.render(f5);
		bone18.render(f5);
		bone17.render(f5);
		bone16.render(f5);
		bone15.render(f5);
		bone14.render(f5);
		bone13.render(f5);
		bb_main.render(f5);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}